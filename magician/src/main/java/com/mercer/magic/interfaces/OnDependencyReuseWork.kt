package com.mercer.magic.interfaces

import com.mercer.magic.extensions.DependencyReusePluginExtension
import org.gradle.api.Project

/**
 * @author :Mercer
 * @Created on 2025/05/01.
 * @Description:
 *   本地模块预发布
 */
interface OnDependencyReuseWork {
    fun apply(target: Project)
}