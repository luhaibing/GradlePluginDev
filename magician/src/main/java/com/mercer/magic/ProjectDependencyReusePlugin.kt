package com.mercer.magic

import com.mercer.magic.interfaces.impl.DependencyReuseGradleAGP7Impl
import com.mercer.magic.interfaces.impl.DependencyReuseGradleAGP8Impl
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author      Mercer
 * @Created     2025/05/05.
 * @Description:
 *   本地依赖替换为本地仓库依赖
 */
class ProjectDependencyReusePlugin :Plugin<Project>{

    override fun apply(target: Project) {
        System.err.println("Not yet implemented")
        val gradleVersion = target.gradle.gradle.gradleVersion.split(".").firstOrNull()?.toInt() ?: 7
        val work = if (gradleVersion < 8) {
            DependencyReuseGradleAGP7Impl()
        } else {
            DependencyReuseGradleAGP8Impl()
        }
        work.apply(target)
    }

}