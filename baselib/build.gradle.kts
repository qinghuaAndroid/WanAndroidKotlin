plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.hilt)
}

android {
    namespace = "com.wan.baselib"

    compileSdk {
        version = release(37)
    }

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }

    buildFeatures {
        buildConfig = true
        dataBinding = true
    }
}

dependencies {
    // AndroidX and Material
    api(libs.androidx.appcompat)
    api(libs.androidx.core.ktx)
    api(libs.material)
    api(libs.androidx.constraintlayout)
    api(libs.androidx.recyclerview)

    // Navigation and lifecycle
    api(libs.androidx.navigation.fragment.ktx)
    api(libs.androidx.navigation.ui.ktx)
    api(libs.androidx.lifecycle.viewmodel.ktx)
    api(libs.androidx.lifecycle.livedata.ktx)
    api(libs.androidx.startup.runtime)

    // UI utilities
    api(libs.splitties.activities)
    api(libs.splitties.fragments)
    api(libs.splitties.appctx)
    api(libs.splitties.views)
    api(libs.splitties.dimensions)
    api(libs.splitties.resources)
    api(libs.circleimageview)
    api(libs.baserecyclerviewadapterhelper)
    api(libs.library)
    api(libs.photoview)
    api(libs.loadsir)
    api(libs.banner)
    api(libs.labelsview)
    api(libs.color)
    api(libs.lifecycle)

    // Networking
    api(libs.retrofit)
    api(libs.converter.gson)
    api(libs.adapter.rxjava3)
    api(platform(libs.okhttp.bom))
    api(libs.okhttp)
    api(libs.logging.interceptor)
    api(libs.persistentcookiejar)
    api(libs.agentweb.core)

    // Reactive programming
    api(libs.rxjava)
    api(libs.rxandroid)
    api(libs.rxbinding)
    api(libs.rxlifecycle.components)

    // Image loading
    api(libs.glide)
    ksp(libs.ksp)
    api(libs.coil)
    api(libs.coil.gif)
    api(libs.coil.svg)

    // Persistence and dependency injection
    api(libs.hilt.android)
    ksp(libs.hilt.compiler)
    api(libs.mmkv)

    // Utilities
    api(libs.logger)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}
