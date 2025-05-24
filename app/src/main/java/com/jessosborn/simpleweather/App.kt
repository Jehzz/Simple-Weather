package com.jessosborn.simpleweather

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
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
		Log.d("SimpleWeather", "schedulePeriodicUpdate()")
		val updateRequest = PeriodicWorkRequestBuilder<ForecastWeatherWorker>(
			repeatInterval = 15,
			repeatIntervalTimeUnit = TimeUnit.MINUTES
		).build()

		WorkManager.getInstance(this).enqueue(updateRequest)
	}
}
