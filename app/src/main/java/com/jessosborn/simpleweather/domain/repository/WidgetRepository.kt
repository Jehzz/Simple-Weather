package com.jessosborn.simpleweather.domain.repository

import android.content.Context
import com.jessosborn.simpleweather.domain.db.dao.WeatherSnapshotDao
import com.jessosborn.simpleweather.domain.remote.responses.WeatherSnapshot
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WidgetRepository @Inject constructor(
	@ApplicationContext private val context: Context,
	private val weatherSnapshotDao: WeatherSnapshotDao
) {
	@EntryPoint
	@InstallIn(SingletonComponent::class)
	interface RepositoryEntryPoint {
		fun widgetRepository(): WidgetRepository
	}

	fun addWeather(weatherSnapshot: List<WeatherSnapshot>) {
		weatherSnapshotDao.insert(weatherSnapshot)
	}

	fun deleteWeather(weatherSnapshot: List<WeatherSnapshot>) {
		weatherSnapshotDao.delete(weatherSnapshot)
	}

	fun getWeather(): Flow<List<WeatherSnapshot>> {
		return weatherSnapshotDao.getWeather()
	}

	companion object {
		fun getRepository(context: Context) =
			EntryPoints.get(
				context,
				RepositoryEntryPoint::class.java
			).widgetRepository()
	}
}