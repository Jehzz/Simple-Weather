package com.jessosborn.simpleweather.domain.repository

import android.content.Context
import android.content.res.Resources
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidgetManager
import com.jessosborn.simpleweather.R
import com.jessosborn.simpleweather.domain.db.dao.WeatherSnapshotDao
import com.jessosborn.simpleweather.domain.remote.OpenWeatherEndpoint
import com.jessosborn.simpleweather.domain.remote.responses.CurrentWeather
import com.jessosborn.simpleweather.domain.remote.responses.ForecastWeather
import com.jessosborn.simpleweather.domain.remote.responses.Main
import com.jessosborn.simpleweather.domain.remote.responses.Sys
import com.jessosborn.simpleweather.domain.remote.responses.WeatherData
import com.jessosborn.simpleweather.domain.remote.responses.WeatherSnapshot
import com.jessosborn.simpleweather.domain.remote.responses.Wind
import com.jessosborn.simpleweather.utils.DataStoreUtil
import com.jessosborn.simpleweather.view.compose.widget.WeatherWidget
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class WeatherRepositoryTest {

    private lateinit var repository: WeatherRepository
    private val context: Context = mockk(relaxed = true)
    private val service: OpenWeatherEndpoint = mockk()
    private val dao: WeatherSnapshotDao = mockk()
    private val glanceManager: GlanceAppWidgetManager = mockk()
    private val resources: Resources = mockk()

    @Before
    fun setup() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        
        mockkObject(DataStoreUtil)
        
        every { context.resources } returns resources
        every { resources.getString(R.string.api_key) } returns "fake_api_key"
        
        // Mocking WeatherWidget constructor and update method
        mockkConstructor(WeatherWidget::class)
        coEvery { anyConstructed<WeatherWidget>().update(any(), any()) } just Runs

        repository = WeatherRepository(context, service, dao, glanceManager)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `fetchCurrentData returns success when service response is successful`() = runTest {
        val currentWeather = createFakeCurrentWeather()
        coEvery {
            service.getCurrentWeather(any(), any(), any())
        } returns Response.success(currentWeather)

        val result = repository.fetchCurrentData("12345", "US", "metric")

        assertTrue(result.isSuccess)
        assertEquals(currentWeather, result.getOrNull())
    }

    @Test
    fun `fetchCurrentData returns failure when service response is unsuccessful`() = runTest {
        coEvery {
            service.getCurrentWeather(any(), any(), any())
        } returns Response.error(404, "Not Found".toResponseBody())

        val result = repository.fetchCurrentData("12345", "US", "metric")

        assertTrue(result.isFailure)
    }

    @Test
    fun `fetchForecastData returns cached data when cache is valid`() = runTest {
        val zip = "12345"
        val units = "metric"
        val snapshots = listOf(createFakeWeatherSnapshot(zip, units, System.currentTimeMillis()))
        
        coEvery { dao.getForecast(zip, units) } returns flowOf(snapshots)
        every { DataStoreUtil.getRefreshTime(context) } returns flowOf(2) // 2 hours
        coEvery { glanceManager.getGlanceIds(WeatherWidget::class.java) } returns emptyList()

        val result = repository.fetchForecastData(zip, "US", units)

        assertTrue(result.isSuccess)
        assertEquals(snapshots, result.getOrNull()?.list)
        coVerify(exactly = 0) { service.getForecastWeather(any(), any(), any()) }
    }

    @Test
    fun `fetchForecastData fetches from network when cache is expired`() = runTest {
        val zip = "12345"
        val units = "metric"
        // 5 hours ago (expired if refresh time is 2 hours)
        val expiredTime = System.currentTimeMillis() - (5 * 60 * 60 * 1000)
        val snapshots = listOf(createFakeWeatherSnapshot(zip, units, expiredTime))
        
        coEvery { dao.getForecast(zip, units) } returns flowOf(snapshots)
        coEvery { dao.deleteForecast(zip, units) } just Runs
        every { DataStoreUtil.getRefreshTime(context) } returns flowOf(2)
        
        val networkForecast = ForecastWeather(listOf(createFakeWeatherSnapshot(zip, units, System.currentTimeMillis())))
        coEvery { service.getForecastWeather(any(), any(), any()) } returns Response.success(networkForecast)
        coEvery { dao.replaceForecast(any(), any(), any()) } just Runs
        coEvery { glanceManager.getGlanceIds(WeatherWidget::class.java) } returns emptyList()

        val result = repository.fetchForecastData(zip, "US", units)

        assertTrue(result.isSuccess)
        coVerify { dao.deleteForecast(zip, units) }
        coVerify { service.getForecastWeather("$zip,US", "fake_api_key", units) }
        coVerify { dao.replaceForecast(zip, units, any()) }
    }

    @Test
    fun `fetchForecastData returns failure when network call fails and no cache`() = runTest {
        val zip = "12345"
        val units = "metric"
        
        coEvery { dao.getForecast(zip, units) } returns flowOf(emptyList())
        coEvery { service.getForecastWeather(any(), any(), any()) } returns Response.error(500, "Server Error".toResponseBody())

        val result = repository.fetchForecastData(zip, "US", units)

        assertTrue(result.isFailure)
    }

    private fun createFakeCurrentWeather() = CurrentWeather(
        name = "Test City",
        main = Main(temp = 20f, temp_min = 15f, temp_max = 25f, humidity = "50"),
        sys = Sys(country = "US", sunrise = "1000", sunset = "2000"),
        weather = listOf(WeatherData(1, "Cloudy", "scattered clouds", "03d")),
        wind = Wind(speed = "5", deg = "180")
    )

    private fun createFakeWeatherSnapshot(zip: String, units: String, createdAt: Long) = WeatherSnapshot(
        zip = zip,
        units = units,
        dt = "123456789",
        dt_txt = "2023-10-27 12:00:00",
        main = Main(temp = 20f, temp_min = 15f, temp_max = 25f, humidity = "50"),
        weather = listOf(WeatherData(1, "Cloudy", "scattered clouds", "03d")),
        pop = 0.1f,
        createdAt = createdAt
    )
}
