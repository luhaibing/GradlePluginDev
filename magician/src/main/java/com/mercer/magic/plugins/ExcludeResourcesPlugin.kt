package com.mercer.magic.plugins

import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.gradle.BaseExtension
import com.mercer.magic.ANDROID_PLUGINS
import com.mercer.magic.extensions.ExcludeResourcesExtensions
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByName

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

        target.extensions.getByType(AndroidComponentsExtension::class.java).apply {
            if (target.properties["forceTransform"] as String? == "true") {
                if (pluginVersion.major >= 8) {
//                    Logger.error("agpVersion >= 8.0.0, forceTransform=true失效")
                    println()
                } else {
//                    pluginType = PluginType.Transformer
//                    Logger.warn("hook强制采用transform")
                    println()
                }
            }
        }

        // val android = target.extensions["android"] as BaseExtension
        // android.registerTransform(HookTransformer())

        /*
        arget.extensions.getByType(AndroidComponentsExtension::class.java)?.apply {
            pluginType = PluginType.TransformAction
            if(target.properties["forceTransform"] as String? == "true") {
                if(pluginVersion.major >= 8) {
                    Logger.error("agpVersion >= 8.0.0, forceTransform=true失效")
                } else {
                    pluginType = PluginType.Transformer
                    Logger.warn("hook强制采用transform")
                }
            }
        } ?: {
            pluginType = PluginType.Transformer
        }
         */
//        target.afterEvaluate {
//            val android: BaseExtension = extensions["android"] as BaseExtension
//
//            // AppExtension
//            // BaseAppModuleExtension
//
//            // com.android.build.gradle.LibraryExtension
//
//            // BaseExtension
//            println(android)
//        }
        // TODO("Not yet implemented")

        val ext = target.extensions.getByType(AndroidComponentsExtension::class.java)
        ext.onVariants {
            it.instrumentation.transformClassesWith(
                HookAsmVisitorFactory::class.java,
                InstrumentationScope.ALL
            ) { params ->
                println()
            }
        }

    }

}