package com.jessosborn.simpleweather.di

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.room.Room
import com.jessosborn.simpleweather.domain.db.WeatherSnapshotDb
import com.jessosborn.simpleweather.domain.db.dao.WeatherSnapshotDao
import com.jessosborn.simpleweather.domain.remote.OpenWeatherEndpoint
import com.jessosborn.simpleweather.domain.repository.IWeatherRepository
import com.jessosborn.simpleweather.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideWeatherRepository(
		@ApplicationContext context: Context,
		network: OpenWeatherEndpoint,
		dao: WeatherSnapshotDao,
		glanceManager: GlanceAppWidgetManager
	): IWeatherRepository {
		return WeatherRepository(
			context = context,
			service = network,
			weatherSnapshotDao = dao,
			glanceManager = glanceManager
		)
	}

	@Provides
	@Singleton
	fun providesAppDatabase(@ApplicationContext context: Context): WeatherSnapshotDb {
		return Room
			.databaseBuilder(context = context, klass = WeatherSnapshotDb::class.java, name = "current_weather")
			.build()
	}

	@Provides
	fun providesCurrentWeatherDao(db: WeatherSnapshotDb): WeatherSnapshotDao {
		return db.weatherSnapshotDao()
	}

	@Provides
	@Singleton
	fun providesWidgetManager(@ApplicationContext context: Context): GlanceAppWidgetManager {
		return GlanceAppWidgetManager(context)
	}
}
