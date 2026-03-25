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
import androidx.work.ListenableWorker.Result as WorkerResult

@HiltWorker
class ForecastWeatherWorker
    @AssistedInject
    constructor(
        @Assisted private val context: Context,
        @Assisted private val workerParams: WorkerParameters,
        val weatherRepository: IWeatherRepository,
    ) : CoroutineWorker(context, workerParams) {
        override suspend fun doWork(): WorkerResult {
            Log.d(TAG, "doWork - Run attempt: $runAttemptCount")

            val zipcodes = DataStoreUtil.getZips(context).first()
            val units = DataStoreUtil.getUnits(context).first()

            if (zipcodes.isEmpty()) {
                Log.d(TAG, "No zipcodes to update")
                return WorkerResult.success()
            }

            var anyFailures = false

            zipcodes.forEach { zip ->
                Log.d(TAG, "Updating zipcode $zip")
                val result =
                    weatherRepository.fetchForecastData(
                        zip = zip,
                        country = getCountryFromZip(zip),
                        units = units.name,
                        forceRefresh = true,
                    )

                if (result.isFailure) {
                    Log.e(TAG, "Zipcode $zip failed to update: ${result.exceptionOrNull()?.message}")
                    anyFailures = true
                }
            }

            return if (anyFailures) {
                // If it's the first few attempts, retry. Otherwise, fail so we don't loop forever.
                if (runAttemptCount < 3) {
                    Log.d(TAG, "Some updates failed, retrying...")
                    WorkerResult.retry()
                } else {
                    Log.d(TAG, "Max retries reached, giving up for this cycle")
                    WorkerResult.failure()
                }
            } else {
                Log.d(TAG, "All zipcodes updated successfully")
                WorkerResult.success()
            }
        }

        companion object {
            internal const val TAG = "ForecastWeatherWorker"
        }
    }
