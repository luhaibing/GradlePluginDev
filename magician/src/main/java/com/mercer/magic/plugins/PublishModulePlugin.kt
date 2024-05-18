package com.mercer.magic.plugins

import com.mercer.magic.LIBRARY_PLUGINS
import com.mercer.magic.extensions.ModuleExtensions
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * author:  mercer
 * date:    2024/5/15
 * desc:
 *   本地模块发布
 */
class PublishModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        println("PublishModulePlugin")
        target.pluginManager.apply("maven-publish")
        if (LIBRARY_PLUGINS.none { target.plugins.hasPlugin(it) }) {
            return
        }
//        target.pluginManager.hasPlugin("")
//        target.afterEvaluate {
//            publishing(rootProject.extensions.getByType(ModuleExtensions::class.java))
//        }
    }

    private fun Project.publishing(module: ModuleExtensions) {
//        extensions.configure<PublishingExtension> {
//            publications.create<MavenPublication>("dev") {
//                groupId = module.groupId
//                artifactId = project.levelName()
//                version = module.version
//                from(if ("java" in components.names) components["java"] else components["debug"])
//            }
//            repositories {
//                maven(module.path)
//            }
//            tasks.register("${project.name}Record") {
//                doLast {
//                    println("记录资源状态>>>done.")
//                    val sourceSetContainer = project.extensions["sourceSets"] as SourceSetContainer
//                    val mainSourceSets = sourceSetContainer.getByName("main")
//                    val allJava = mainSourceSets.allJava
//                    mainSourceSets?.java?.files?.toList()
//                    println()
//
//                    // 获取 Kotlin 扩展
//                    val kotlinExtension = project.extensions["kotlin"] as? KotlinProjectExtension
//                    kotlinExtension?.sourceSets?.forEach { sourceSet ->
//                        println("Found Kotlin source set: ${sourceSet.name}")
//                        // 操作 sourceSet，比如获取 Kotlin 文件
//                        sourceSet.kotlin.files.forEach { file ->
//                            println("Kotlin File: ${file.absolutePath}")
//                        }
//                    }
//                    println()
//                    println()
//                    println()
//
//                }
//            }
//            val publishTask = tasks.findByName("publish")!!
//            val recordTask = tasks.findByName("${project.name}Record")!!
//            publishTask.finalizedBy(recordTask)
//        }
    }

}