package com.jessosborn.simpleweather.domain.repository

import android.content.Context
import com.jessosborn.simpleweather.domain.db.dao.WeatherSnapshotDao
import com.jessosborn.simpleweather.domain.remote.responses.WeatherSnapshot
import com.jessosborn.simpleweather.utils.DataStoreUtil
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class WidgetRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val weatherSnapshotDao: WeatherSnapshotDao,
) {
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface RepositoryEntryPoint {
        fun widgetRepository(): WidgetRepository
    }

    fun getWeather(): Flow<List<WeatherSnapshot>> {
        val units = runBlocking { DataStoreUtil.getUnits(context).first() }
        val zips = runBlocking { DataStoreUtil.getZips(context).first() }
        val primaryZip = zips.firstOrNull() ?: ""
        return weatherSnapshotDao.getForecast(primaryZip, units.name)
    }

    companion object {
        fun getRepository(context: Context) =
            EntryPoints.get(
                context,
                RepositoryEntryPoint::class.java,
            ).widgetRepository()
    }
}
