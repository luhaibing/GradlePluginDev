package com.mercer.magic

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.api.AndroidSourceSet
import com.android.build.gradle.internal.api.DefaultAndroidSourceDirectorySet
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mercer.magic.bean.FileRes
import com.mercer.magic.bean.ModuleRes
import com.mercer.magic.bean.Named
import com.mercer.magic.extensions.DependencyReusePluginExtension
import okio.Buffer
import okio.sink
import okio.source
import org.gradle.api.Project
import org.gradle.api.internal.artifacts.dependencies.DefaultProjectDependency
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import java.io.File

/**
 * @author :Mercer
 * @Created on 2025/05/01.
 * @Description:
 *   工具方法
 */
fun requireExtensionNotNull(project: Project): DependencyReusePluginExtension {
    val extension = project.extensions.findByType(
        DependencyReusePluginExtension::class.java
    ) ?: throw IllegalArgumentException("未指定本地模块复用的插件的参数 .")
    try {
        extension.groupId
    } catch (e: Exception) {
        // e.printStackTrace()
        throw IllegalArgumentException("未指定本地依赖的 groupId .")
    }
    try {
        extension.respUri
    } catch (e: Exception) {
        // e.printStackTrace()
        throw IllegalArgumentException("未指定本地依赖的仓库地址 .")
    }
    return extension
}

val Project.named: Named
    get() {
        /*
        // 1
        var cur: Project? = project
        val rootProject = project.rootProject
        val values = arrayListOf<CharSequence>()
        while (cur != null && cur != rootProject) {
            values.add(cur.name)
            cur = cur.parent
        }
        return values.reversed().joinToString("_")
        // 2
        return project.path.split(":").filter(String::isNotEmpty).joinToString("_")
        */
        return Named(name = project.name, path = project.path)
    }

fun Project.findProjectDependencies(): List<Named> = project.configurations
    .asSequence()
    .filter {
        it.name in arrayOf("implementation", "api")
    }
    .map { con ->
        con.dependencies.filterIsInstance<DefaultProjectDependency>()
    }
    .flatten()
    .toList()
    .map {
        it.dependencyProject.named
    }
    .toList()

fun Project.javaRes(): ModuleRes {
    val files = arrayListOf<FileRes>()
    val subProjects = arrayListOf<Named>()
    val javaExtension = extensions.findByName("sourceSets") as? SourceSetContainer
    val kotlinExtension = extensions.findByName("kotlin") as? KotlinProjectExtension
    val javaPredicate: (SourceSet) -> Unit = {
        files.addAll(it.java.files.map(FileRes::Java))
        files.addAll(it.resources.files.map(FileRes::Resources))
    }
    val kotlinPredicate: (KotlinSourceSet) -> Unit = {
        files.addAll(it.kotlin.files.map(FileRes::Kotlin))
        files.addAll(it.resources.files.map(FileRes::Resources))
    }
    if (project.buildFile.exists()) {
        files.add(FileRes.Build(project.buildFile))
    }
    javaExtension?.findByName(MAIN)?.let(javaPredicate)
    javaExtension?.findByName(DEBUG)?.let(javaPredicate)
    kotlinExtension?.sourceSets?.findByName(MAIN)?.let(kotlinPredicate)
    kotlinExtension?.sourceSets?.findByName(DEBUG)?.let(kotlinPredicate)
    subProjects.addAll(findProjectDependencies())
    return ModuleRes.Java(name = project.named, files = files, subProjects = subProjects, projectDir = projectDir.absolutePath)
}

@Suppress("DEPRECATION")
fun Project.androidRes(): ModuleRes {
    val files = arrayListOf<FileRes>()
    val subProjects = arrayListOf<Named>()
    if (project.buildFile.exists()) {
        files.add(FileRes.Build(project.buildFile))
    }
    val android = extensions.findByName("android") as? BaseExtension
    val predicate: (AndroidSourceSet) -> Unit = {
        if (it.manifest.srcFile.exists()) {
            files.add(FileRes.Manifest(it.manifest.srcFile))
        }
        files.addAll(it.java.getSourceFiles().files.map(FileRes::Java))
        files.addAll(it.aidl.getSourceFiles().files.map(FileRes::Aidl))
        files.addAll(it.res.getSourceFiles().files.map(FileRes::Res))
        files.addAll(it.assets.getSourceFiles().files.map(FileRes::Assets))
        files.addAll(((it.kotlin as? DefaultAndroidSourceDirectorySet)?.getSourceFiles()?.files ?: emptyList()).map(FileRes::Kotlin))
    }
    android?.sourceSets?.findByName(MAIN)?.let(predicate)
    android?.sourceSets?.findByName(DEBUG)?.let(predicate)
    subProjects.addAll(findProjectDependencies())
    return ModuleRes.Android(name = project.named, files = files, subProjects = subProjects, projectDir = projectDir.absolutePath)
}

fun jsonFile2res(value: File): ModuleRes? {
    if (value.exists().not()) {
        return null
    }
    val buffer = Buffer()
    value.source().use {
        buffer.writeAll(it)
    }
    val json = buffer.readString(Charsets.UTF_8)
    val type = object : TypeToken<ModuleRes>() {}.type
    return try {
        Gson().fromJson<ModuleRes>(json, type)
    } catch (e: Exception) {
        System.err.println("$value 解析成 ModuleRes 出错 : $e")
        e.printStackTrace()
        null
    }
}

fun record(res: ModuleRes, respFile: File) {
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
