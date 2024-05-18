package com.mercer.magic.plugins

import com.mercer.magic.ANDROID_PLUGINS
import com.mercer.magic.extensions.ExcludeResourcesExtensions
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * author:  mercer
 * date:    2024/5/15
 * desc:
 *   排除指定类
 */
class ExcludeResourcesPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        println("ExcludeClassPlugin")
        target.extensions.create("excludes", ExcludeResourcesExtensions::class.java)
        if (ANDROID_PLUGINS.none { target.pluginManager.hasPlugin(it) }) {
            return
        }
//        target.afterEvaluate {
//            val android: BaseExtension = extensions["android"] as BaseExtension
//            // android.registerTransform()
//            // android.()
//            // AppExtension
//            // BaseAppModuleExtension
//
//            // com.android.build.gradle.LibraryExtension
//
//            // BaseExtension
//
//
//
//
//            println(android)
//        }



        // TODO("Not yet implemented")
    }

}