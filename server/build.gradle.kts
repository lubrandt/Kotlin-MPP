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
}

version = "unspecified"

repositories {
    mavenCentral()
}

kotlin {
    jvm()
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(project(":common"))

                implementation(kotlin("stdlib-jdk8"))
                implementation("io.ktor:ktor-server-netty:$ktorVersion")
                implementation("io.ktor:ktor-server-core:$ktorVersion")
                implementation("io.ktor:ktor-html-builder:$ktorVersion")
                implementation("ch.qos.logback:logback-classic:$logbackVersion")

                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serializationVersion")
                implementation("io.ktor:ktor-client-serialization-jvm:$ktorVersion")
                implementation("io.ktor:ktor-auth:$ktorVersion")
                implementation("io.ktor:ktor-locations:$ktorVersion")

                //db things
                implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
                implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
                implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
                implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
                implementation("com.h2database:h2:$h2Version")

                //client logging
                implementation("io.ktor:ktor-client-logging-jvm:$ktorVersion")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit5"))
                implementation("org.junit.jupiter:junit-jupiter:5.6.2")
                implementation("io.ktor:ktor-server-test-host:$ktorVersion")
            }
        }
    }
}

