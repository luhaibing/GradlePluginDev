package com.mercer.magic.plugins

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.internal.api.DefaultAndroidSourceDirectorySet
import com.google.gson.Gson
import com.google.protobuf.gradle.ProtobufExtension
import com.google.protobuf.gradle.tasks.ProtoSourceSet
import com.mercer.magic.LIBRARY_PLUGINS
import com.mercer.magic.bean.Summary
import com.mercer.magic.extensions.ModuleExtensions
import com.mercer.magic.levelName
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.maven
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import java.io.File
import java.io.FileOutputStream
import java.nio.charset.Charset

/**
 * author:  mercer
 * date:    2024/5/15
 * desc:
 *   本地模块发布
 */
class PublishModulePlugin : Plugin<Project> {
    companion object {
        // 主体
        const val MAIN = "main"
    }

    override fun apply(target: Project) {
        println("PublishModulePlugin")
        target.pluginManager.apply("maven-publish")
        if (LIBRARY_PLUGINS.none { target.plugins.hasPlugin(it) }) {
            return
        }
        target.pluginManager.hasPlugin("")
        target.afterEvaluate {
            publishing(rootProject.extensions.getByType(ModuleExtensions::class.java))
        }
    }

    private fun Project.publishing(module: ModuleExtensions) {
        extensions.configure<PublishingExtension> {
            publications.create<MavenPublication>("dev") {
                groupId = module.groupId
                artifactId = project.levelName()
                version = module.version
                from(if ("java" in components.names) components["java"] else components["debug"])
            }
            repositories {
                maven(module.path)
            }
            tasks.register("record") {
                doLast {
                    record(module)
                }
                ///////////////////////////////////////////////
            }
            val publishTask = tasks.findByName("publish")!!
            val recordTask = tasks.findByName("record")!!
            publishTask.finalizedBy(recordTask)
        }
    }


    /**
     *
     */
    private fun Project.record(extension: ModuleExtensions) {
        // TODO: 2024/5/16 暂不考虑变体和渠道
        val files: MutableList<File> = arrayListOf()
        // java
        files.addAll(javaRes())
        // kotlin
        files.addAll(kotlinRes())
        // android
        files.addAll(androidRes())
        // proto
        files.addAll(protoRes())
        val summary = Summary(extension, this, files)
        val json = Gson().toJson(summary)
        val path = arrayListOf(
            extension.folder().absolutePath,
            name,
            extension.version,
            "summary.json"
        ).joinToString(File.separator)
        val file = File(path)
        file.parentFile.mkdirs()
        file.deleteOnExit()
        val outputStream = FileOutputStream(file)
        outputStream.write(json.toByteArray(charset = Charset.forName("utf-8")))
        outputStream.flush()
        outputStream.close()
    }

    private fun Project.javaRes(): List<File> {
        val sourceSets = extensions.findByName("sourceSets") as? SourceSetContainer
        val sourceSet = sourceSets?.findByName(MAIN) ?: return emptyList()
        return arrayListOf<File>().apply {
            addAll(sourceSet.java.files)
            addAll(sourceSet.resources.files)
        }
    }

    private fun Project.kotlinRes(): List<File> {
        val extension = extensions.findByName("kotlin") as? KotlinProjectExtension
        val sourceSet = extension?.sourceSets?.findByName(MAIN) ?: return emptyList()
        return arrayListOf<File>().apply {
            addAll(sourceSet.kotlin.files)
            addAll(sourceSet.resources.files)
        }
    }

    private fun Project.androidRes(): List<File> {
        val extension = extensions.findByName("android") as? BaseExtension
        val sourceSet = extension?.sourceSets?.findByName(MAIN) ?: return emptyList()
        val kotlin = sourceSet.kotlin as DefaultAndroidSourceDirectorySet
        val java = sourceSet.java
        val aidl = sourceSet.aidl
        val res = sourceSet.res
        return arrayListOf<File>().apply {
            add(sourceSet.manifest.srcFile)
            addAll(java.getSourceFiles().files)
            addAll(kotlin.getSourceFiles().files)
            addAll(aidl.getSourceFiles().files)
            addAll(res.getSourceFiles().files)
        }
    }

    private fun Project.protoRes(): List<File> {
        val extension = extensions.findByName("protobuf") as? ProtobufExtension
        extension ?: return emptyList()
        val sourceSetsFunc = ProtobufExtension::class.java.getDeclaredMethod("getSourceSets")
        sourceSetsFunc.isAccessible = true
        val sourceSets = sourceSetsFunc.invoke(extension) as NamedDomainObjectContainer<*>
        val sourceSet = sourceSets.findByName(MAIN) as ProtoSourceSet
        return arrayListOf<File>().apply {
            addAll(sourceSet.proto.files)
        }
    }

}