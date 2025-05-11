package com.mercer.magic.extensions

import java.io.File
import java.net.URI
import kotlin.properties.Delegates

/**
 * @author      Mercer
 * @Created     2025/05/05.
 * @Description:
 *   本地模块依赖复用的插件的扩展信息
 */
open class DependencyReusePluginExtension {
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