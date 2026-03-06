package com.jessosborn.simpleweather

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.jessosborn.simpleweather.utils.DataStoreUtil
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class App @Inject constructor() : Application(), Configuration.Provider {

	@Inject
	lateinit var workerFactory: HiltWorkerFactory

	override fun getWorkManagerConfiguration() = Configuration.Builder().setWorkerFactory(workerFactory).build()

	override fun onCreate() {
		super.onCreate()
		schedulePeriodicUpdate()
	}

	private fun schedulePeriodicUpdate() {
		CoroutineScope(Dispatchers.Default).launch {
			val interval = DataStoreUtil.getRefreshTime(this@App).first()
			val timeUnit = TimeUnit.HOURS
			Log.d("SimpleWeather", "Scheduling update every $interval $timeUnit")

			val updateRequest = PeriodicWorkRequestBuilder<ForecastWeatherWorker>(
				interval.toLong(),
				timeUnit
			)
				.addTag(ForecastWeatherWorker.TAG)
				.build()

			val workManager = WorkManager.getInstance(this@App)

			workManager.enqueueUniquePeriodicWork(
				ForecastWeatherWorker.TAG,
				ExistingPeriodicWorkPolicy.UPDATE, // UPDATE to change interval if the user modified it
				updateRequest
			)
		}
	}
}
