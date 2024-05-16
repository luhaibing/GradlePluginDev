package com.mercer.magic

import org.gradle.api.Project
import java.text.SimpleDateFormat
import java.util.Locale
import java.io.File
import java.security.MessageDigest

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


// 扩展函数，用于将 ByteArray 转为 16 进制 MD5 字符串形式
fun ByteArray.toHex(): String {
    return joinToString("") { "%02x".format(it) }
}

// 使用 MD5 算法得到文件的 hash 值
fun File.md5(): String {
    if (!this.exists() || !this.isFile) throw IllegalArgumentException("Not a file or doesn't exist!")

    val digest = MessageDigest.getInstance("MD5")
    this.inputStream().use { fis ->
        val buffer = ByteArray(1024 * 1024 * 2)  // 2MB buffer
        var length = fis.read(buffer)
        while (length != -1) {
            digest.update(buffer, 0, length)
            length = fis.read(buffer)
        }
    }
    return digest.digest().toHex()
}