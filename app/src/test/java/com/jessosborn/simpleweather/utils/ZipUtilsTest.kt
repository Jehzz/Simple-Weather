package com.jessosborn.simpleweather.utils

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ZipUtilsTest {

    @Test
    fun isValidZip() {
        assertTrue(VALID_USA_ZIP.isValidZip())
        assertTrue(VALID_CAN_ZIP.isValidZip())
        assertTrue(VALID_UK_3_ZIP.isValidZip())
        assertTrue(VALID_UK_4_ZIP.isValidZip())
    }

    @Test
    fun validUsZipIsUsZip() {
        assertTrue(VALID_USA_ZIP.isUsZip())
    }

    @Test
    fun nonUsZipIsNotUsZip() {
        assertFalse(VALID_CAN_ZIP.isUsZip())
        assertFalse(VALID_UK_3_ZIP.isUsZip())
        assertFalse(VALID_UK_4_ZIP.isUsZip())
    }

    @Test
    fun validCanZipIsCanZip() {
        assertTrue(VALID_CAN_ZIP.isCanadianZip())
    }

    @Test
    fun nonCanZipIsNotCanZip() {
        assertFalse(VALID_USA_ZIP.isCanadianZip())
        assertFalse(VALID_UK_3_ZIP.isCanadianZip())
        assertFalse(VALID_UK_4_ZIP.isCanadianZip())
    }

    @Test
    fun validUkZipIsUkZip() {
        assertTrue(VALID_UK_4_ZIP.isUkZip())
        assertTrue(VALID_UK_3_ZIP.isUkZip())
    }

    @Test
    fun nonUkZipIsNotUkZip() {
        assertFalse(VALID_USA_ZIP.isUkZip())
    }

    companion object {
        const val VALID_UK_4_ZIP = "WC2N"
        const val VALID_UK_3_ZIP = "PR6"
        const val VALID_USA_ZIP = "10168"
        const val VALID_CAN_ZIP = "M4V"
    }
}
