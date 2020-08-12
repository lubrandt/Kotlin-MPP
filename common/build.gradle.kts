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
}



kotlin {
    js {
        browser()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:$serializationVersion")

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:$coroutineVersion")

                implementation("io.ktor:ktor-serialization:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization:$ktorVersion")
            }
        }
//        val commonTest by getting {
//            dependencies {
//
//            }
//        }
    }
}
