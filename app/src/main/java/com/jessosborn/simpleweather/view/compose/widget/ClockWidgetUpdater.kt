package com.jessosborn.simpleweather.view.compose.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

class ClockWidgetUpdater(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        // Update all instances of the StencilWidget
        val manager = GlanceAppWidgetManager(context)
        val glanceIds = manager.getGlanceIds(ClockWidget::class.java)
        glanceIds.forEach { glanceId ->
            ClockWidget().update(context, glanceId)
        }

        // Schedule the next update at the top of the next minute
        scheduleNextUpdate(context)

        return Result.success()
    }

    companion object {
        private const val WORK_NAME = "ClockWidgetUpdateWorker"

        fun scheduleNextUpdate(context: Context) {
            val currentTime = System.currentTimeMillis()
            val delay = 60000 - (currentTime % 60000)

            val updateRequest = OneTimeWorkRequestBuilder<ClockWidgetUpdater>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                updateRequest
            )
        }
    }
}
