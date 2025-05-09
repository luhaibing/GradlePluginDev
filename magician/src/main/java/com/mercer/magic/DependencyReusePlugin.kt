package com.mercer.magic

import com.mercer.magic.bean.ModuleRes
import com.mercer.magic.bean.ProjectDependencySnapshot
import com.mercer.magic.extensions.DependencyReusePluginExtensions
import okio.Buffer
import okio.sink
import okio.use
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import java.io.File


class DependencyReusePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val taskNames = target.gradle.startParameter.taskNames
        println("gradle start ： ${taskNames.joinToString(",", prefix = "[", postfix = "]")}")
        val assemblePredicate: (String) -> Boolean = {
            it.matches(ASSEMBLE_REGEX)
        }
        val publishPredicate: (String) -> Boolean = {
            it.matches(PUBLISH_REGEX)
        }
        val assembleTasks = taskNames.filter(assemblePredicate)
        val publishTasks = taskNames.filter(publishPredicate)
        val rootProject = target.rootProject
        rootProject.extensions.create("dependencyReuse", DependencyReusePluginExtensions::class.java)
        if (assembleTasks.isEmpty() && publishTasks.isEmpty()) {
            System.err.println("本次 gradle 任务不包含 [构建任务、发布任务].")
        }
        target.gradle.projectsEvaluated {
            val extension = rootProject.extensions.findByType(DependencyReusePluginExtensions::class.java)
            try {
                extension?.groupId
            } catch (e: Exception) {
                // e.printStackTrace()
                throw IllegalArgumentException("未指定本地依赖的 groupId .")
            }
            try {
                extension?.respUri
            } catch (e: Exception) {
                // e.printStackTrace()
                throw IllegalArgumentException("未指定本地依赖的仓库地址 .")
            }
            val subprojects = target.rootProject.subprojects.toList()
            val currentResMap = hashMapOf<String, ModuleRes?>()
            /*
            val previousResMap = hashMapOf<String, ModuleRes?>()
            if (respFile.exists()) {
                val listFiles = respFile.listFiles() ?: emptyArray()
                for (f in listFiles) {
                    if (f.isDirectory || f.name.endsWith(".json").not()) {
                        continue
                    }
                    val res = jsonFile2res(f) ?: continue
                    previousResMap[res.named.artifactName] = res
                }
            }
            */
            for (project in subprojects) {
                if (project.buildFile.exists().not()) {
                    continue
                }
                val pluginManager = project.pluginManager
                val res = if (pluginManager.hasPlugin("com.android.library")) {
                    project.androidRes()
                } else if (pluginManager.hasPlugin("java") || pluginManager.hasPlugin("kotlin")) {
                    project.javaRes()
                } else if (pluginManager.hasPlugin("com.android.application")) {
                    null
                } else {
                    System.err.println("模块 ${project.name} 类型错误,无法获取生成依赖信息的快照.")
                    null
                }
                currentResMap[project.named.artifactName] = res
            }
            for (project in subprojects) {
                val pluginManager = project.pluginManager
                if (pluginManager.hasPlugin("com.android.application")) {
                    replaceModule(project, currentResMap, extension!!)
                } else {
                    val artifact = project.named.artifactName
                    val currentRes = currentResMap[artifact]
                    if (currentRes != null) {
                        publishModule(project, currentRes, extension!!, assembleTasks)
                    }
                }
            }
        }
        val subprojects = target.rootProject.subprojects.toList()
        for (project in subprojects) {
            val pluginManager = project.pluginManager
            if (pluginManager.hasPlugin("com.android.application")) {
                continue
            }
            if (pluginManager.hasPlugin("maven-publish")) {
                continue
            }
            project.pluginManager.apply("maven-publish")
        }
    }

    private fun publishModule(project: Project, currentRes: ModuleRes, extension: DependencyReusePluginExtensions, assembleTasks: List<String>) {
        val group = extension.groupId
        val respFile = extension.respFile
        val artifact = project.named.artifactName
        val path = arrayOf(group.replace(".", File.separator), currentRes.named.artifactName, currentRes.lastModifiedTime).joinToString(File.separator)
        with(project) {
            extensions.configure(PublishingExtension::class.java) { publishing ->
                publishing.repositories.maven {
                    it.url = respFile.toURI()
                }
                publishing.publications.create("dev", MavenPublication::class.java) {
                    it.groupId = group
                    it.artifactId = artifact
                    it.version = currentRes.lastModifiedTime
                    it.from(if ("java" in components.names) components.getByName("java") else components.getByName("debug"))
                }
                val recordTask = tasks.create("record") {
                    it.group = "publishing"
                    it.doLast {
                        record(currentRes, respFile)
                    }
                }
                tasks.matching {
                    it.group == "publishing" && it.name.startsWith("publish")
                }.configureEach {
                    it.dependsOn(recordTask)
                }
                val publishTask = tasks.findByName("publishDevPublicationToMavenRepository")
                // TODO: 简单的认为只要目录文件地址存在，依赖就存在
                if (File(respFile, path).exists().not() && publishTask != null && extension.autoPublish) {
                    for (element in assembleTasks) {
                        val splits = element.split(":").filter(String::isNotBlank)
                        if (splits.size == 2) {
                            val (m, t) = splits
                            val assembleDebugTask = rootProject.project(m).tasks.findByName(t) ?: continue
                            assembleDebugTask.dependsOn(publishTask)
                        }
                    }
                }
            }
        }
    }

    private fun replaceModule(project: Project, currentResMap: Map<String, ModuleRes?>, extension: DependencyReusePluginExtensions) {
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
            val currentRes = currentResMap[artifact] ?: continue
            val path = arrayOf(group.replace(".", File.separator), currentRes.named.artifactName, currentRes.lastModifiedTime).joinToString(File.separator)
            if (File(respFile, path).exists().not()) {
                continue
            }
            includes.add(ProjectDependencySnapshot(named = named, res = currentRes, group = group))
        }
        project.configurations.configureEach { configuration ->
            configuration.resolutionStrategy { strategy ->
                strategy.dependencySubstitution { substitution ->
                    with(substitution) {
                        for (element in includes) {
                            substitute(project(element.named.path)).using(module(element.toString()))
                        }
                        // substitute module('org.gradle:api') using project(':api')
                        substitute(module("com.mercer:base_ui")).using(project(":base:ui"))
                    }
                }
            }
            println()
            if (configuration.name in arrayOf("implementation", "api")) {
                val dependencies = configuration.dependencies
                println(dependencies)
            }
            println()
        }
    }

    private fun record(res: ModuleRes, respFile: File) {
        val json = res.toJson().toString()
        val file = File(respFile, "${res.named.artifactName}.json")
        file.parentFile.mkdirs()
        file.deleteOnExit()
        val buffer = Buffer()
        buffer.write(json.toByteArray(charset = Charsets.UTF_8))
        file.sink().use {
            it.write(buffer, buffer.size)
        }
        println(json)
    }

}