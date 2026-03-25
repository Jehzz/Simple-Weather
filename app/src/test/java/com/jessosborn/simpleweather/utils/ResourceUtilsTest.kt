package com.jessosborn.simpleweather.utils

import com.jessosborn.simpleweather.R
import org.junit.Assert.assertEquals
import org.junit.Test

class ResourceUtilsTest {
    @Test
    fun `getIconResource returns correct drawable for valid codes`() {
        assertEquals(R.drawable.icon_01d_t, getIconResource("01d"))
        assertEquals(R.drawable.icon_01n_t, getIconResource("01n"))
        assertEquals(R.drawable.icon_02d_t, getIconResource("02d"))
        assertEquals(R.drawable.icon_09d_t, getIconResource("09d"))
        assertEquals(R.drawable.icon_13d_t, getIconResource("13d"))
        assertEquals(R.drawable.icon_50n_t, getIconResource("50n"))
    }

    @Test
    fun `getIconResource returns default drawable for invalid codes`() {
        assertEquals(R.drawable.icon_01d_t, getIconResource("invalid"))
        assertEquals(R.drawable.icon_01d_t, getIconResource(""))
    }
}
