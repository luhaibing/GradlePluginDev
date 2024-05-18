package com.mercer.magic.plugins

import com.mercer.magic.extensions.ModuleExtensions
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * author:  mercer
 * date:    2024/5/15
 * desc:
 *   本地模块替换
 */
class ReplaceModulePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        println("ReplaceModulePlugin")
        target.extensions.create("module", ModuleExtensions::class.java)
//        target.afterEvaluate {
//            val (includes: MutableList<Pair<String, String>>, excludes: MutableList<Pair<String, String>>) = parse()
//            replace(includes, excludes)
//        }
    }

    private fun Project.replace(
        includes: MutableList<Pair<String, String>>,
        excludes: MutableList<Pair<String, String>>
    ) {
//        subprojects {
//            configurations.configureEach {
//                resolutionStrategy {
//                    dependencySubstitution {
//                        includes.forEach { (m, d) ->
//                            substitute(project(":$m")).using(module(d))
//                        }
//                        excludes.forEach { (m, d) ->
//                            substitute(module(d)).using(project(":$m"))
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private fun Project.parse(): Pair<MutableList<Pair<String, String>>, MutableList<Pair<String, String>>> {
//        val module = extensions.getByType(ModuleExtensions::class.java)
//        val children = subprojects.toList()
//        // 已发布缓存
//        val includes: MutableList<Pair<String, String>> = arrayListOf()
//        // 未发布缓存
//        val excludes: MutableList<Pair<String, String>> = arrayListOf()
//        val ignores: MutableList<String> = arrayListOf()
//        for (child in children) {
//            val levelName = child.levelName()
//            // 只处理 含有 java 或者 com.android.library 插件的模块
//
//            // if (!child.pluginManager.hasPlugin(JAVA_PLUGIN) && !child.pluginManager.hasPlugin(ANDROID_LIBRARY_PLUGIN)
//            if (LIBRARY_PLUGINS.none { child.plugins.hasPlugin(it) }) {
//                ignores.add(levelName)
//                continue
//            }
//            val code = arrayOf(module.groupId, levelName, module.version).joinToString(":")
//
//            // TODO:  检查模块的文件是否存在
//            // if ((!child.projectDir.exists() || !child.buildFile.exists() || !child.file("src").exists() || !File(module.folder(), levelName).exists())) {
//            if (arrayOf(
//                    child.projectDir, child.buildFile, child.file("src"), module.target(levelName)
//                ).any { !it.exists() }
//            ) {
//                excludes.add(levelName to code)
//                continue
//            }
//            includes.add(levelName to code)
//        }
//        return Pair(includes, excludes)
    }

}