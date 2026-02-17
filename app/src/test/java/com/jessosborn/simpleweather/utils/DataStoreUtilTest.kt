package com.jessosborn.simpleweather.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.jessosborn.simpleweather.domain.Theme
import com.jessosborn.simpleweather.domain.Units
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class DataStoreUtilTest {

    @get:Rule
    val tmpFolder = TemporaryFolder()

    private val context: Context = mockk()
    private lateinit var testDataStore: DataStore<Preferences>

    @Before
    fun setup() {
        // Create a real DataStore using a temporary file
        testDataStore = PreferenceDataStoreFactory.create(
            produceFile = { File(tmpFolder.newFolder(), "test.preferences_pb") }
        )
        
        mockkStatic("com.jessosborn.simpleweather.utils.DataStoreUtilKt")
        every { context.datastore } returns testDataStore
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getTheme returns Light by default`() = runTest {
        val theme = DataStoreUtil.getTheme(context).first()
        assertEquals(Theme.Light, theme)
    }

    @Test
    fun `saveTheme and getTheme returns correct value`() = runTest {
        DataStoreUtil.saveTheme(context, Theme.Dark)
        val theme = DataStoreUtil.getTheme(context).first()
        assertEquals(Theme.Dark, theme)
    }

    @Test
    fun `getUnits returns Imperial by default`() = runTest {
        val units = DataStoreUtil.getUnits(context).first()
        assertEquals(Units.Imperial, units)
    }

    @Test
    fun `saveUnits and getUnits returns correct value`() = runTest {
        DataStoreUtil.saveUnits(context, Units.Metric)
        val units = DataStoreUtil.getUnits(context).first()
        assertEquals(Units.Metric, units)
    }

    @Test
    fun `getZip returns empty string by default`() = runTest {
        val zip = DataStoreUtil.getZip(context).first()
        assertEquals("", zip)
    }

    @Test
    fun `saveZip and getZip returns correct value`() = runTest {
        val testZip = "12345"
        DataStoreUtil.saveZip(context, testZip)
        val zip = DataStoreUtil.getZip(context).first()
        assertEquals(testZip, zip)
    }

    @Test
    fun `getRefreshTime returns 2 by default`() = runTest {
        val refreshTime = DataStoreUtil.getRefreshTime(context).first()
        assertEquals(2, refreshTime)
    }

    @Test
    fun `saveRefreshTime and getRefreshTime returns correct value`() = runTest {
        val testTime = 5
        DataStoreUtil.saveRefreshTime(context, testTime)
        val refreshTime = DataStoreUtil.getRefreshTime(context).first()
        assertEquals(testTime, refreshTime)
    }
}
