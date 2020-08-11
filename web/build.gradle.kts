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
        val jsMain by getting {
            dependencies {
                implementation(project(":common"))

                implementation(kotlin("stdlib-js"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:$serializationVersion")

                // ktor client
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-js:$ktorVersion")
                implementation("io.ktor:ktor-client-json-js:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization-js:$ktorVersion")
                implementation("io.ktor:ktor-client-auth-js:$ktorVersion")
                implementation("io.ktor:ktor-client-logging-js:$ktorVersion")

                //React, React DOM + Wrappers
                implementation("org.jetbrains:kotlin-react:$react-pre.94-kotlin-1.3.70")
                implementation("org.jetbrains:kotlin-react-dom:$react-pre.94-kotlin-1.3.70")
                implementation("org.jetbrains:kotlin-react-router-dom:4.3.1-pre.94-kotlin-1.3.70")
                implementation(npm("react", react))
                implementation(npm("react-dom", react))
                implementation(npm("react-router-dom"))

                //Kotlin Styled, sometimes not found?
                implementation("org.jetbrains:kotlin-css-js:1.0.0-pre.94-kotlin-1.3.70") //not found?! todo: hä?
                implementation("org.jetbrains:kotlin-styled:1.0.0-pre.94-kotlin-1.3.70")
                implementation(npm("styled-components", "5.1.1"))
                implementation(npm("react-is", react))
                implementation(npm("inline-style-prefixer", "6.0.0"))

                //Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:$coroutineVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")

                // React packages/modules
                implementation(npm("react-minimal-pie-chart", "7.3.1")) // piechart, versions > 7.3.1 don't work

                // fix abort-controller & text-encoding modules not found
                // weird dependency hell to get rid of warnings
                // even the official handson from kotlin has these
                implementation(npm("text-encoding")) // deprecated
                implementation(npm("abort-controller"))
                implementation(npm("utf-8-validate"))
                implementation(npm("bufferutil"))
                implementation(npm("fs"))

            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}