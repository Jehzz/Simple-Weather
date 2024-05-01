package com.jessosborn.simpleweather.dagger

import android.content.Context
import com.jessosborn.simpleweather.domain.remote.OpenWeatherEndpoint
import com.jessosborn.simpleweather.utils.isOnline
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


	@Provides
	@Singleton
	fun provideClient(@ApplicationContext context: Context): OkHttpClient {
		val client = OkHttpClient.Builder()
			.cache(Cache(File(context.cacheDir, "cache"), (5 * 1024 * 1024).toLong()))
			.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
			.addNetworkInterceptor(
				Interceptor { chain ->
					val response = chain.proceed(chain.request())
					val cacheControl = CacheControl.Builder()
						.maxAge(10, TimeUnit.MINUTES)
						.build()
					response.newBuilder()
						.header("Cache-Control", cacheControl.toString())
						.build()
				}
			)
			.addInterceptor(
				Interceptor { chain ->
					var request = chain.request()
					if (!isOnline(context)) {
						val cacheControl = CacheControl.Builder()
							.maxStale(1, TimeUnit.DAYS)
							.build()
						request = request.newBuilder()
							.cacheControl(cacheControl)
							.build()
					}
					chain.proceed(request)
				}
			)
		return client.build()
	}

	@Provides
	@Singleton
	fun provideOpenWeatherEndpoint(retrofit: Retrofit): OpenWeatherEndpoint {
		return retrofit.create(OpenWeatherEndpoint::class.java)
	}

	@Provides
	@Singleton
	fun provideRetrofit(client: OkHttpClient): Retrofit {
		return Retrofit.Builder()
			.client(client)
			.baseUrl(OpenWeatherEndpoint.baseApiUrl)
			.addConverterFactory(GsonConverterFactory.create())
			.build()
	}

}