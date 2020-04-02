/*
 * based on an Idea from https://stackoverflow.com/questions/53544441/why-i-cannot-access-project-properties-in-plugins-section-of-build-gradle-kts
 * with https://github.com/tehbilly/gradle-kotlin-dsl-plugin-versioning
 */

object Versions {
    // Library versions
    val kotlinVersion = "1.3.71"
    val ktorVersion = "1.3.2"
    val logback = "1.2.3"
    val mainLibVersion = "0.20.0"
    val kotlinExposedVersion = "0.23.1"
    val h2Version = "1.4.200"
//    val klockVersion = "1.10.3"

    // Plugin versions
    object Plugins {
        // Can be changed if we want to use a version other than one matching the library version
        val kotlin = kotlinVersion
    }
}