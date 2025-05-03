plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    id("maven-publish")
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}
dependencies {
    // implementation(gradleKotlinDsl())
    implementation(gradleApi())
    implementation(localGroovy())
    // https://mvnrepository.com/artifact/org.jetbrains.kotlin.jvm/org.jetbrains.kotlin.jvm.gradle.plugin
    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:2.0.21")
    // https://mvnrepository.com/artifact/com.android.application/com.android.application.gradle.plugin
    implementation("com.android.application:com.android.application.gradle.plugin:8.9.2")
    // https://mvnrepository.com/artifact/com.android.library/com.android.library.gradle.plugin
    implementation("com.android.library:com.android.library.gradle.plugin:8.9.2")
    implementation("com.google.protobuf:protobuf-gradle-plugin:0.9.4")
    implementation("com.google.code.gson:gson:2.10.1")
    // https://mvnrepository.com/artifact/com.squareup.okio/okio
    implementation("com.squareup.okio:okio:3.11.0")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("plugins") {
                groupId = "com.mercer"
                artifactId = "magician"
                version = "1.0.0"
                from(components["java"])
            }
        }
        repositories {
            maven {
                url = File(project.rootDir, "plugin-resp").toURI()
            }
        }
    }
}