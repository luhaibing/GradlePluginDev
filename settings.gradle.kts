pluginManagement {
    repositories {
        maven {
            url = File(rootDir, "resp").toURI()
        }
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven {
            url = File(rootDir, "resp").toURI()
        }
        google()
        mavenCentral()
    }
}

rootProject.name = "DependencyReuseDemo"
include(":app")
include(":magician")
