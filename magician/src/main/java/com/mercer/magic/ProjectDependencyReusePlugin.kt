package com.mercer.magic

import com.mercer.magic.interfaces.impl.DependencyReuseGradle7Impl
import com.mercer.magic.interfaces.impl.DependencyReuseGradle8Impl
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author :Mercer
 * @Created on 2025/05/06.
 * @Description:
 *   本地依赖
 */
class ProjectDependencyReusePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        System.err.println("Not yet implemented")
        val gradleVersion = target.gradle.gradle.gradleVersion.split(".").firstOrNull()?.toInt() ?: 7
        val work = if (gradleVersion < 8) {
            DependencyReuseGradle7Impl()
        } else {
            DependencyReuseGradle8Impl()
        }
        work.apply(target)
    }

}