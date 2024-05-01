package com.jessosborn.simpleweather.dagger

import android.content.Context
import com.jessosborn.simpleweather.domain.remote.OpenWeatherEndpoint
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
        network: OpenWeatherEndpoint
    ): WeatherRepository {
        return WeatherRepository(
            context = context,
            service = network
        )
    }
}
