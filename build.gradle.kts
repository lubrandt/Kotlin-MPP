/*
 * This file was generated by the Gradle 'init' task.
 *
 * This is a general purpose Gradle build.
 * Learn how to create Gradle builds at https://guides.gradle.org/creating-new-gradle-builds
 */

//variables
val ktorVersion = "1.3.2"
val logbackVersion = "1.2.3"

plugins {
    kotlin("multiplatform") version "1.3.71"
}

repositories {
    jcenter()
    maven("https://dl.bintray.com/kotlin/ktor")
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
    maven("https://kotlin.bintray.com/kotlin-js-wrappers/")
    maven("https://plugins.gradle.org/m2/")
    mavenCentral()
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
//                implementation("io.ktor:ktor-html-builder:$ktorVersion")
                implementation("ch.qos.logback:logback-classic:$logbackVersion")
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

                //React, React DOM + Wrappers (chapter 3)
//                implementation("org.jetbrains:kotlin-react:16.9.0-pre.89-kotlin-1.3.60")
//                implementation("org.jetbrains:kotlin-react-dom:16.9.0-pre.89-kotlin-1.3.60")
//                implementation(npm("react", "16.12.0"))
//                implementation(npm("react-dom", "16.12.0"))
//
//                //Kotlin Styled (chapter 3)
//                implementation("org.jetbrains:kotlin-styled:1.0.0-pre.90-kotlin-1.3.61")
//                implementation(npm("styled-components", "5.0.1"))
//                implementation(npm("react-is", "16.12.0"))
//                implementation(npm("inline-style-prefixer", "5.1.2"))
//
//                //Video Player (chapter 7)
//                implementation(npm("react-player", "1.15.2"))
//
//                //Share Buttons (chapter 7)
//                implementation(npm("react-share", "4.0.1"))
//
//                //Coroutines (chapter 8)
//                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.3.3")
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
    main = "de.innosystec.kuestion.MainKt"
    kotlin {
        val main = targets["jvm"].compilations["main"]
        dependsOn(main.compileAllTaskName)
        classpath(
            { main.output.allOutputs.files },
            { configurations["jvmRuntimeClasspath"] }
        )
    }
}