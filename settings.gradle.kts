pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ping"
include(":app")
include(":firebase")
include(":data")
include(":utils")
include(":repository")
include(":screens")
include(":screens:frx")
include(":screens:common")
