package com.jessosborn.simpleweather

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.jessosborn.simpleweather.domain.repository.IWeatherRepository
import com.jessosborn.simpleweather.utils.DataStoreUtil
import com.jessosborn.simpleweather.utils.getCountryFromZip
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class ForecastWeatherWorker @AssistedInject constructor(
	@Assisted private val context: Context,
	@Assisted private val workerParams: WorkerParameters,
	val weatherRepository: IWeatherRepository
) : CoroutineWorker(context, workerParams) {

	override suspend fun doWork(): Result {
		Log.d(TAG, "doWork")

		val zip = DataStoreUtil.getZip(context).first()
		val units = DataStoreUtil.getUnits(context).first()

		val result = weatherRepository.fetchForecastData(
			zip = zip,
			country = getCountryFromZip(zip),
			units = units.name
		)

		when {
			result.isSuccess -> {
				Log.d(TAG, "Success")
				return Result.success()
			}
			else -> {
				Log.d(TAG, "Error")
				return Result.failure()
			}
		}
	}
	companion object {
		internal const val TAG = "ForecastWeatherWorker"
	}
}