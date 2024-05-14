plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    // https://mvnrepository.com/artifact/com.google.code.gson/gson
    implementation("com.google.code.gson:gson:2.10.1")
    implementation(gradleKotlinDsl())
    implementation(gradleApi())
    implementation(localGroovy())
    // https://mvnrepository.com/artifact/org.jetbrains.kotlin.jvm/org.jetbrains.kotlin.jvm.gradle.plugin
    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:1.9.0")
    // https://mvnrepository.com/artifact/com.android.application/com.android.application.gradle.plugin
    implementation("com.android.application:com.android.application.gradle.plugin:8.4.0")
    // https://mvnrepository.com/artifact/com.android.library/com.android.library.gradle.plugin
    implementation("com.android.library:com.android.library.gradle.plugin:8.4.0")
}

//publishement.gradle
//replacement.gradle
// publishement.properties
// replacement.properties

gradlePlugin {
    plugins {
        register("PublishModule") {
            id = "publish.module"
            implementationClass = "com.mercer.magic.plugins.PublishModulePlugin"
        }
        register("ReplaceModule") {
            id = "replace.module"
            implementationClass = "com.mercer.magic.plugins.ReplaceModulePlugin"
        }
        register("ExcludeResources") {
            id = "exclude.resources"
            implementationClass = "com.mercer.magic.plugins.ExcludeResourcesPlugin"
        }
    }
}