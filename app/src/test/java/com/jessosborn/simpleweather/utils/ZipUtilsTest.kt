package com.jessosborn.simpleweather.utils

import com.jessosborn.simpleweather.domain.CountryCode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ZipUtilsTest {

    @Test
    fun `isValidZip returns true for valid zips`() {
        assertTrue("10168".isValidZip())
        assertTrue("M4V".isValidZip())
        assertTrue("WC2N".isValidZip())
        assertTrue("PR6".isValidZip())
    }

    @Test
    fun `isValidZip returns false for invalid zips`() {
        assertFalse("1234".isValidZip())
        assertFalse("123456".isValidZip())
        assertFalse("ABC DE".isValidZip())
        assertFalse("".isValidZip())
    }

    @Test
    fun `isInvalidZip returns correct value`() {
        assertTrue("1234".isInvalidZip())
        assertFalse("10168".isInvalidZip())
    }

    @Test
    fun `isUsZip validates correctly`() {
        assertTrue("10168".isUsZip())
        assertFalse("1016".isUsZip())
        assertFalse("101689".isUsZip())
        assertFalse("ABCDE".isUsZip())
    }

    @Test
    fun `isCanadianZip validates correctly`() {
        assertTrue("M4V".isCanadianZip())
        assertTrue("m4v".isCanadianZip())
        assertFalse("M4V1".isCanadianZip())
        assertFalse("123".isCanadianZip())
    }

    @Test
    fun `isUkZip validates correctly`() {
        assertTrue("WC2N".isUkZip())
        assertTrue("PR6".isUkZip())
        assertTrue("wc2n".isUkZip())
        assertFalse("WC2N12".isUkZip())
        assertFalse("1234".isUkZip())
    }

    @Test
    fun `getCountryFromZip returns correct codes`() {
        assertEquals(CountryCode.US.code, getCountryFromZip("10168"))
        assertEquals(CountryCode.CANADA.code, getCountryFromZip("M4V"))
        assertEquals(CountryCode.UK.code, getCountryFromZip("WC2N"))
        assertEquals("NULL", getCountryFromZip("INVALID"))
    }
}
