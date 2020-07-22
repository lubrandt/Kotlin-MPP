package de.innosystec.kuestion.network

object CommonDateUtil

expect class CommonDate

/**
 * checks if the given date is less than the current date
 */
expect fun CommonDateUtil.compareToCurrentDate(z: CommonDate): Boolean
// todo: ist diese funktion testbar in commonTest? wird eine implementation benötigt? shared test for expected values
