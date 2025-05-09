package com.mercer.magic.interfaces.impl

import com.google.gson.Gson
import com.mercer.magic.ASSEMBLE_REGEX
import com.mercer.magic.PUBLISH_REGEX
import com.mercer.magic.androidRes
import com.mercer.magic.bean.ModuleRes
import com.mercer.magic.bean.Named
import com.mercer.magic.bean.ProjectDependencySnapshot
import com.mercer.magic.extensions.DependencyReusePluginExtension
import com.mercer.magic.findProjectDependencies
import com.mercer.magic.interfaces.OnDependencyReuseWork
import com.mercer.magic.javaRes
import com.mercer.magic.named
import com.mercer.magic.record
import com.mercer.magic.requireExtensionNotNull
import org.gradle.api.Project
import org.gradle.api.internal.artifacts.dependencies.DefaultExternalModuleDependency
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import java.io.File
import java.util.HashMap

/**
 * @author :Mercer
 * @Created on 2025/05/01.
 * @Description:
 *
 */
class DependencyReuseGradle7Impl : OnDependencyReuseWork {
    override fun apply(target: Project) {
        val rootProject = target.rootProject
        val gradle = rootProject.gradle
        val taskNames = gradle.startParameter.taskNames
        println("gradle start ： ${taskNames.joinToString(",", prefix = "[", postfix = "]")}")
        rootProject.extensions.create("dependencyReuse", DependencyReusePluginExtension::class.java)
        val assemblePredicate: (String) -> Boolean = {
            it.matches(ASSEMBLE_REGEX)
        }
        val publishPredicate: (String) -> Boolean = {
            it.matches(PUBLISH_REGEX)
        }
        val assembleTasks = taskNames.filter(assemblePredicate)
        val publishTasks = taskNames.filter(publishPredicate)
        if (assembleTasks.isEmpty() && publishTasks.isEmpty()) {
            System.err.println("本次 gradle 任务不包含 [构建任务、发布任务].")
        }
        val subprojects = rootProject.subprojects.toList()
        val resMap = hashMapOf<String, ModuleRes?>()
        for (project in subprojects) {
            if (project.buildFile.exists().not()) {
                continue
            }
            project.afterEvaluate {
                val pluginManager = it.pluginManager
                val extension = requireExtensionNotNull(rootProject)
                if (pluginManager.hasPlugin("com.android.application")) {
                    return@afterEvaluate
                }
                if (pluginManager.hasPlugin("maven-publish")) {
                    return@afterEvaluate
                }
                val res = if (pluginManager.hasPlugin("com.android.library")) {
                    it.androidRes()
                } else if (pluginManager.hasPlugin("java") || pluginManager.hasPlugin("kotlin")) {
                    it.javaRes()
                } else if (pluginManager.hasPlugin("com.android.application")) {
                    null
                } else {
                    System.err.println("模块 ${it.name} 类型错误,无法获取生成依赖信息的快照.")
                    null
                }
                resMap[it.named.artifactName] = res
                if (res != null) {
                    publishModule(it, res, extension)
                }
            }
        }
        gradle.projectsEvaluated {
            if (resMap.isEmpty()) {
                System.err.println("不存在本地模块的编译缓存.")
            }
            System.err.println(resMap.values.joinToString("\n\t", "[\n\t", "\n]"))
            val extension = requireExtensionNotNull(rootProject)
            val filters = subprojects.filter {
                it.pluginManager.hasPlugin("com.android.application").not()
            }
            for (project in subprojects) {
                replaceModule(project, resMap, extension, filters)
            }
        }
    }

    private fun publishModule(project: Project, res: ModuleRes, extension: DependencyReusePluginExtension) {
        val group = extension.groupId
        val respFile = extension.respFile
        val artifact = project.named.artifactName
        val pluginManager = project.pluginManager
        pluginManager.apply("maven-publish")
        project.afterEvaluate {
            project.extensions.configure(PublishingExtension::class.java) { publishing ->
                publishing.repositories.maven {
                    it.url = respFile.toURI()
                }
                val components = project.components
                publishing.publications.create("dev", MavenPublication::class.java) {
                    it.groupId = group
                    it.artifactId = artifact
                    it.version = res.lastModifiedTime
                    it.from(if ("java" in components.names) components.getByName("java") else components.getByName("debug"))
                }
            }
            val tasks = project.tasks
            val recordTask = tasks.create("record") {
                it.group = "publishing"
                it.doLast {
                    record(res, respFile)
                }
            }
            tasks.matching {
                it.group == "publishing" && it.name.startsWith("publish")
            }.configureEach {
                it.dependsOn(recordTask)
            }
            val path = arrayOf(group.replace(".", File.separator), res.named.artifactName, res.lastModifiedTime).joinToString(File.separator)
            // TODO: 简单的认为只要目录文件地址存在，依赖就存在
            val publishTask = tasks.findByName("publishDevPublicationToMavenRepository")
            if (File(respFile, path).exists().not() && publishTask != null && extension.autoPublish) {
                when (res.type) {
                    ModuleRes.Type.Java -> tasks.findByName("jar")?.finalizedBy(publishTask)
                    ModuleRes.Type.Android -> arrayOf("bundleLibRuntimeToJarDebug", "bundleLibRuntimeToDirDebug").map(tasks::findByName).onEach { t ->
                        t?.finalizedBy(publishTask)
                    }
                }
            }
        }
    }

    private fun replaceModule(project: Project, resMap: HashMap<String, ModuleRes?>, extension: DependencyReusePluginExtension, elements: List<Project>) {
        if (extension.enabled.not()) {
            System.err.println("不启用依赖复用插件.")
            return
        }
        val group = extension.groupId
        val respFile = extension.respFile
        val names = project.findProjectDependencies()
        val includes = arrayListOf<ProjectDependencySnapshot>()
        for (named in names) {
            val artifact = named.artifactName
            val currentRes = resMap[artifact] ?: continue
            val path = arrayOf(group.replace(".", File.separator), currentRes.named.artifactName, currentRes.lastModifiedTime).joinToString(File.separator)
            if (File(respFile, path).exists().not()) {
                continue
            }
            if (project.named.artifactName == artifact) {
                continue
            }
            includes.add(ProjectDependencySnapshot(named = named, res = currentRes, group = group))
        }
        val includePaths = includes.map(ProjectDependencySnapshot::named).map(Named::path)
        val excludes = arrayListOf<Named>()
        for (element in elements) {
            if (element == project) {
                continue
            }
            if (element.buildFile.exists().not()) {
                continue
            }
            val named = element.named
            if (named.path in includePaths) {
                continue
            }
            excludes.add(named)
        }
        System.err.println("name     : ${project.named.path}")
        System.err.println("includes : ${Gson().toJson(includes.map(ProjectDependencySnapshot::named).map(Named::path))}")
        System.err.println("excludes : ${Gson().toJson(excludes.map(Named::path))}")
        System.err.println()
        project.configurations.configureEach { configuration ->
            configuration.resolutionStrategy { strategy ->
                strategy.dependencySubstitution { substitution ->
                    with(substitution) {
                        for (element in includes) {
                            substitute(project(element.named.path)).using(module(element.toString()))
                        }
                        for (element in excludes) {
                            substitute(module(arrayOf(extension.groupId, element.artifactName).joinToString(":"))).using(project(element.path))
                        }
                        // com.mercer:base_common-ui
                        // System.err.println("替换 base_common")
                        // substitute(module("com.mercer:base_common-ui")).using(project(":base:common-ui"))
                    }
                }
            }
        }
    }

}