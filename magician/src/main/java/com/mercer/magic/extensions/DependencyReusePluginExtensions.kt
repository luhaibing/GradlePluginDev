package com.mercer.magic.extensions

import java.io.File
import java.net.URI
import kotlin.properties.Delegates

/**
 * author:  Mercer
 * date:    2025/05/01.
 * desc:
 *   本地依赖复用插件的扩展信息
 */
open class DependencyReusePluginExtensions {
    // 本地依赖的组名
    var groupId: String by Delegates.notNull()
    // 仓库地址
    var respUri: URI by Delegates.notNull()
    // 仓库地址
    var respFile: File
        get() {
            return File(respUri)
        }
        set(value) {
            respUri = value.toURI()
        }
    // 是否启用
    var enabled: Boolean = true
    // 是否随 assembleXXX 类任务自动发布
    var autoPublish: Boolean = true
}