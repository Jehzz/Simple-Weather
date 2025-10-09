package com.jessosborn.simpleweather.di

import com.jessosborn.simpleweather.domain.remote.OpenWeatherEndpoint
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    @Provides
    @Singleton
    fun provideOpenWeatherEndpoint(retrofit: Retrofit): OpenWeatherEndpoint =
        retrofit.create(OpenWeatherEndpoint::class.java)


    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(client)
            .baseUrl(OpenWeatherEndpoint.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}
