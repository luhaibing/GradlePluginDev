package com.mercer.magic

import org.gradle.api.Project
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * author:  mercer
 * date:    2024/5/15
 * desc:
 *
 */

fun Project.levelName(): String {
    var level: Project? = this
    val names: MutableList<String> = arrayListOf()
    while (level != null && level != rootProject) {
        names.add(0, level.name)
        level = level.parent
    }
    return names.joinToString("_")
}

fun String.levelName2projectName(): String {
    return replace("_", ":")
}


val DATA_FORMAT = SimpleDateFormat("yyyy.MM.dd-HH.mm.ss", Locale.ENGLISH)

fun timeMillis2date(value: Long): String {
    return DATA_FORMAT.format(value)
}

fun date2timeMillis(value: String): Long {
    return DATA_FORMAT.parse(value).time
}

const val JAVA_PLUGIN = "java"
const val ANDROID_LIBRARY_PLUGIN = "com.android.library"
const val ANDROID_APPLICATION_PLUGIN = "com.android.application"

val LIBRARY_PLUGINS = arrayOf(JAVA_PLUGIN, ANDROID_LIBRARY_PLUGIN)
val ANDROID_PLUGINS = arrayOf(ANDROID_APPLICATION_PLUGIN, ANDROID_LIBRARY_PLUGIN)