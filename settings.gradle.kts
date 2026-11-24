pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        // Central requests return missing artifacts on this network; prefer its mirror.
        maven("https://maven.aliyun.com/repository/central")
        mavenCentral()
        gradlePluginPortal()
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        maven("https://jitpack.io")
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        // Resolve metadata and JARs from the same working Central mirror.
        maven("https://maven.aliyun.com/repository/central")
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        maven("https://jitpack.io")
        maven("https://maven.aliyun.com/nexus/content/groups/public/")
    }
}

rootProject.name = "WanAndroidKotlin"
include(":app")
include(":baselib")
include(":common")
include(":login")
