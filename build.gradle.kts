//variables
val ktorVersion = Versions.ktorVersion
val logbackVersion = Versions.logback
val serializationVersion = Versions.mainLibVersion
val exposedVersion = Versions.kotlinExposedVersion
val h2Version = Versions.h2Version

plugins {
    kotlin("multiplatform") version Versions.Plugins.kotlin
    kotlin("plugin.serialization") version Versions.Plugins.kotlin
}

repositories {
    mavenCentral()
    maven("https://dl.bintray.com/kotlin/ktor")
//    maven("https://dl.bintray.com/kotlin/kotlin-eap")
    maven("https://kotlin.bintray.com/kotlin-js-wrappers/")
    maven("https://plugins.gradle.org/m2/")
    jcenter()
}

kotlin {
    jvm()
    js {
        browser()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:$serializationVersion")

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.3.5")
            }
        }
        val commonTest by getting {
            dependencies {
//                implementation(kotlin("test-common"))
//                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation("io.ktor:ktor-server-netty:$ktorVersion")
                implementation("io.ktor:ktor-server-core:$ktorVersion")
                implementation("io.ktor:ktor-html-builder:$ktorVersion")
                implementation("ch.qos.logback:logback-classic:$logbackVersion")

                implementation("io.ktor:ktor-serialization:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serializationVersion")

                //db things
                implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
//                compile("org.jetbrains.exposed", "exposed-dao", "0.23.1")
                implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
//                compile("org.jetbrains.exposed", "exposed-jodatime", "0.23.1")
                implementation("com.h2database:h2:$h2Version")
            }
        }
        val jvmTest by getting {
            dependencies {
//                implementation(kotlin("test"))
//                implementation(kotlin("test-junit"))
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:$serializationVersion")

                // ktor client
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-js:$ktorVersion")

                //React, React DOM + Wrappers (chapter 3)
                implementation("org.jetbrains:kotlin-react:16.13.0-pre.94-kotlin-1.3.70")
                implementation("org.jetbrains:kotlin-react-dom:16.13.0-pre.94-kotlin-1.3.70")
                implementation(npm("react", "16.13.0"))
                implementation(npm("react-dom", "16.13.0"))

                //Kotlin Styled (chapter 3)
                implementation("org.jetbrains:kotlin-styled:1.0.0-pre.94-kotlin-1.3.70")
                implementation(npm("styled-components", "5.0.1"))
                implementation(npm("react-is", "16.13.0"))
                implementation(npm("inline-style-prefixer", "6.0.0"))

                //Video Player (chapter 7)
                implementation(npm("react-player"))

                //Share Buttons (chapter 7)
                implementation(npm("react-share"))

//                //Coroutines (chapter 8)
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.3.5")

                //charts
                implementation(npm("react-minimal-pie-chart"))

                // fix abort-controller & text-encoding modules not found
                implementation(npm("text-encoding", "0.7.0")) // deprecated
                implementation(npm("abort-controller", "3.0.0"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}

val run by tasks.creating(JavaExec::class) {
    group = "application"
    main = "de.innosystec.kuestion.JvmMainKt"
    kotlin {
        val main = targets["jvm"].compilations["main"]
        dependsOn(main.compileAllTaskName)
        classpath(
            { main.output.allOutputs.files },
            { configurations["jvmRuntimeClasspath"] }
        )
    }
}