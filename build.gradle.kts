buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.0.0")
    }
}

allprojects {
    repositories {
        jcenter()
        google()
        mavenCentral()
    }
}

/**https://github.com/Kotlin/mpp-example/tree/master/greeting/src
 * https://github.com/AAkira/mpp-example
 * https://github.com/thoutbeckers/kotlin-mpp-example/tree/master/mpp/src

 */