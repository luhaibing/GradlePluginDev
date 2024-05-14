package com.mercer.magic.extensions

import java.io.File
import kotlin.properties.Delegates

/**
 * author:  mercer
 * date:    2024/5/15
 * desc:
 *   本地模块发布的扩展信息
 */
open class ModuleExtensions {
    var path: File by Delegates.notNull()
    var groupId: String by Delegates.notNull()

    // var version: String by "Delegates.notNull()"
    var version: String = "1.0"
    fun folder(): File {
        return File(path, groupId.replace(".", File.separator))
    }

    fun target(value: String): File {
        return File(folder(), value)
    }
}