package com.mercer.magic.bean

import com.google.gson.Gson
import com.mercer.magic.dataFormat
import java.io.File


/**
 * author:  Mercer
 * date:    2025/05/01.
 * desc:
 *   模块资源
 */
data class ModuleRes(
    val named: Named,
    val type: Type,
    val projectDir: String,
    val files: List<FileRes>,
    val subProjects: List<Named>,
    val lastModified: Long = files.maxOfOrNull { it.lastModified } ?: System.currentTimeMillis(),
    val lastModifiedTime: String = dataFormat.format(lastModified)
) {
    enum class Type {
        Java, Android
    }

    @Suppress("FunctionName")
    companion object {
        fun Java(name: Named, files: List<FileRes>, subProjects: List<Named>, projectDir: String): ModuleRes {
            return ModuleRes(named = name, type = Type.Java, projectDir = projectDir, files = files, subProjects = subProjects)
        }

        fun Android(name: Named, files: List<FileRes>, subProjects: List<Named>, projectDir: String): ModuleRes {
            return ModuleRes(named = name, type = Type.Android, projectDir = projectDir, files = files, subProjects = subProjects)
        }
    }

    fun toJson(): CharSequence {
        val files = files.map {
            it.copy(name = it.name.replace(arrayOf(projectDir, File.separator).joinToString(""), "").replace("\\", "/"))
        }
        val bean = copy(files = files, projectDir = projectDir.replace("\\","/"))
        return Gson().toJson(bean)
    }

}