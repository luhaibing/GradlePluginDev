package com.mercer.magic.extensions

import kotlin.properties.Delegates

/**
 * author:  Mercer
 * date:    2025/05/01.
 * desc:
 *   本地模块发布的扩展信息
 */
open class ModuleExtensions {
    var groupId: String by Delegates.notNull()
    var excludes: List<String> = arrayListOf()
}