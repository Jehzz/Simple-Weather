package com.jessosborn.simpleweather

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.jessosborn.simpleweather.utils.DataStoreUtil
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class App @Inject constructor() : Application(), Configuration.Provider {

	@Inject
	lateinit var workerFactory: HiltWorkerFactory

	override fun getWorkManagerConfiguration() =
		Configuration.Builder()
			.setWorkerFactory(workerFactory)
			.build()

	override fun onCreate() {
		super.onCreate()
		schedulePeriodicUpdate()
	}

	private fun schedulePeriodicUpdate() {
		val interval = runBlocking { DataStoreUtil.getRefreshTime(this@App).first() }
		Log.d("SimpleWeather", "schedulePeriodicUpdate() every $interval hours")
		val updateRequest = PeriodicWorkRequestBuilder<ForecastWeatherWorker>(
			repeatInterval = interval.toLong(),
			repeatIntervalTimeUnit = TimeUnit.HOURS
		).build()

		WorkManager.getInstance(this).enqueue(updateRequest)
	}
}
