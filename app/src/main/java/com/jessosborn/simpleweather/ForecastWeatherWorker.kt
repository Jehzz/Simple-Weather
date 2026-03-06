package com.jessosborn.simpleweather

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.jessosborn.simpleweather.domain.remote.responses.ForecastWeather
import com.jessosborn.simpleweather.domain.repository.IWeatherRepository
import com.jessosborn.simpleweather.utils.DataStoreUtil
import com.jessosborn.simpleweather.utils.getCountryFromZip
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import androidx.work.ListenableWorker.Result as WorkerResult
import kotlin.Result as KotlinResult

@HiltWorker
class ForecastWeatherWorker @AssistedInject constructor(
	@Assisted private val context: Context,
	@Assisted private val workerParams: WorkerParameters,
	val weatherRepository: IWeatherRepository
) : CoroutineWorker(context, workerParams) {

	override suspend fun doWork(): WorkerResult {
		Log.d(TAG, "doWork")

		val zipcodes = DataStoreUtil.getZips(context).first()
		val units = DataStoreUtil.getUnits(context).first()
		var result: KotlinResult<ForecastWeather>? = null

		zipcodes.forEach { zip ->
			Log.d(TAG, "Updating zipcode $zip")
			weatherRepository.fetchForecastData(
				zip = zip,
				country = getCountryFromZip(zip),
				units = units.name
			).also { apiResult ->
				result = apiResult
				if (apiResult.isFailure) {
					Log.d(TAG, "Zipcode $zip failed to update")
					return@forEach
				}
			}
		}

		when {
			result?.isSuccess == true -> {
				Log.d(TAG, "Success")
				return WorkerResult.success()
			}
			else -> {
				Log.d(TAG, "Error")
				return WorkerResult.failure()
			}
		}
	}
	companion object {
		internal const val TAG = "ForecastWeatherWorker"
	}
}