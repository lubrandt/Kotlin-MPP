//variables
val ktorVersion = Versions.ktorVersion
val logbackVersion = Versions.logback
val serializationVersion = Versions.mainLibVersion
val exposedVersion = Versions.kotlinExposedVersion
val h2Version = Versions.h2Version
val coroutineVersion = Versions.coroutineVersion
val react = Versions.react

plugins {
    kotlin("multiplatform") version Versions.Plugins.kotlin
    kotlin("plugin.serialization") version Versions.Plugins.kotlin
    id("com.android.application")
}

android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.1")

    defaultConfig {
        applicationId = "com.example.kuestion"
        minSdkVersion(24)
        targetSdkVersion(30)
        versionCode = 1
        versionName =  "1.0"

        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    packagingOptions {
        exclude("META-INF/main.kotlin_module")
    }

}

kotlin {
    android()
    js {
        browser()
    }
    jvm()
    sourceSets {
        val commonMain by getting {

        }
        val commonTest by getting {

        }
    }
}
