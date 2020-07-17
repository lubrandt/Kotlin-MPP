package de.innosystec.kuestion

import de.innosystec.kuestion.CommonDate
import de.innosystec.kuestion.CommonDateUtil
import de.innosystec.kuestion.goalescence
import de.innosystec.kuestion.number
import kotlin.test.*

class CommonTests {
    /**
     * todo: jvm works (how? why?) but js fails with jsBrowserTest Target container is not a DOM element!? (is mocha even installed/used??)
     * https://blog.kotlin-academy.com/testing-common-modules-66b39d641617
     * https://mochajs.org/
     * https://github.com/JetBrains/kotlin-wrappers/tree/master/kotlin-mocha
     * https://github.com/sedovalx/kotlin-mpp-sandbox
     * https://github.com/Kotlin/kotlin-examples/tree/master/gradle/js-tests/mocha
     * https://guides.gradle.org/migrating-build-logic-from-groovy-to-kotlin/
     */
    @Test
    fun testGoalescence() {
        val i = 5
        assertEquals(25, goalescence(i))
    }

    @Test
    fun testNumber() {
        assertEquals(5, number)
    }
}