package com.mercer.magic.interfaces

import org.gradle.api.Project

/**
 * @author      Mercer
 * @Created     2025/05/05.
 * @Description:
 *   本地模块依赖与本地仓库依赖替换
 */
interface OnDependencyReuseWork {
    fun apply(target: Project)
}