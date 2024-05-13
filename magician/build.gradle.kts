/*
plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}
*/
plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

afterEvaluate {
    println()
    val toList = configurations["implementation"].dependencies.toList()
    println(toList)
}