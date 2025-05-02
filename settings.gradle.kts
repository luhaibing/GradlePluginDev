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
include(":base:base")
include(":base:ui")
include(":feature:ymodem")
include(":feature:dev")
include(":feature:weather")
include(":services:database")
include(":services:api")
include(":device:boat")
include(":device:irrigate")
include(":device:swimming")
include(":device:station")
include(":device:common")
