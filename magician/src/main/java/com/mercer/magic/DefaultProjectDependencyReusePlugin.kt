package com.mercer.magic

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author :Mercer
 * @Created on 2025/05/06.
 * @Description:
 *   本地依赖
 */
class DefaultProjectDependencyReusePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        // System.err.println("Not yet implemented")
        val rootProject = target.rootProject
        val gradle = rootProject.gradle
        gradle.projectsEvaluated {
            val subprojects = rootProject.subprojects
            for (project in subprojects) {
                replaceModule(project)
            }
        }
    }

    private fun replaceModule(target: Project) {
        target.configurations.configureEach { configuration ->
            configuration.resolutionStrategy { strategy ->
                strategy.dependencySubstitution { substitution ->
                    with(substitution) {
                        substitute(project("base:common-ui")).using(module("com.aiper:base_common-ui:1.0.0"))
                        substitute(project("base:base")).using(module("com.aiper:base_base:1.0.0"))
                        substitute(project("business:customer")).using(module("com.aiper:business_customer:1.0.0"))
                        substitute(project("business:device-common")).using(module("com.aiper:business_device-common:1.0.0"))
                        /*
                        substitute(project("business:device-hydrocomm")).using(module("com.aiper:business_device-hydrocomm:1.0.0"))
                        substitute(project("business:device-i")).using(module("com.aiper:business_device-i:1.0.0"))
                        substitute(project("business:device-r")).using(module("com.aiper:business_device-r:1.0.0"))
                        substitute(project("business:device-s")).using(module("com.aiper:business_device-s:1.0.0"))
                        substitute(project("business:device-x")).using(module("com.aiper:business_device-x:1.0.0"))
                        */
                        substitute(project("business:mine")).using(module("com.aiper:business_mine:1.0.0"))
                        substitute(project("business:sobot-chat")).using(module("com.aiper:business_sobot-chat:1.0.0"))
                        substitute(project("common-service:database")).using(module("com.aiper:common-service_database:1.0.0"))
                        substitute(project("common-service:device-api")).using(module("com.aiper:common-service_device-api:1.0.0"))
                        substitute(project("features:dev")).using(module("com.aiper:features_dev:1.0.0"))
                        substitute(project("features:weather")).using(module("com.aiper:features_weather:1.0.0"))
                        substitute(project("features:ymodem")).using(module("com.aiper:features_ymodem:1.0.0"))
                    }
                }
            }
        }
    }

    private fun replaceModule2(target: Project) {
        target.configurations.configureEach { configuration ->
            configuration.resolutionStrategy { strategy ->
                strategy.dependencySubstitution { substitution ->
                    with(substitution) {
                        substitute(project(":base:common-ui")).using(module("com.aiper:base_common-ui:1.0.0"))
                        substitute(project(":base:base")).using(module("com.aiper:base_base:1.0.0"))
                        substitute(project(":business:customer")).using(module("com.aiper:business_customer:1.0.0"))
                        substitute(project(":business:device-common")).using(module("com.aiper:business_device-common:1.0.0"))
                        /*
                        substitute(project(":business:device-hydrocomm")).using(module("com.aiper:business_device-hydrocomm:1.0.0"))
                        substitute(project(":business:device-i")).using(module("com.aiper:business_device-i:1.0.0"))
                        substitute(project(":business:device-r")).using(module("com.aiper:business_device-r:1.0.0"))
                        substitute(project(":business:device-s")).using(module("com.aiper:business_device-s:1.0.0"))
                        substitute(project(":business:device-x")).using(module("com.aiper:business_device-x:1.0.0"))
                        */
                        substitute(project(":business:mine")).using(module("com.aiper:business_mine:1.0.0"))
                        substitute(project(":business:sobot-chat")).using(module("com.aiper:business_sobot-chat:1.0.0"))
                        substitute(project(":common-service:database")).using(module("com.aiper:common-service_database:1.0.0"))
                        substitute(project(":common-service:device-api")).using(module("com.aiper:common-service_device-api:1.0.0"))
                        substitute(project(":features:dev")).using(module("com.aiper:features_dev:1.0.0"))
                        substitute(project(":features:weather")).using(module("com.aiper:features_weather:1.0.0"))
                        substitute(project(":features:ymodem")).using(module("com.aiper:features_ymodem:1.0.0"))
                    }
                }
            }
        }
    }

}