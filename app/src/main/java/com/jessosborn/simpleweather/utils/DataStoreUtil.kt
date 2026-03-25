package com.jessosborn.simpleweather.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.jessosborn.simpleweather.domain.Theme
import com.jessosborn.simpleweather.domain.Units
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.datastore: DataStore<Preferences> by preferencesDataStore("settings")

object DataStoreUtil {
    const val USER_UNITS = "units"
    const val USER_ZIP = "zip"
    const val USER_ZIPS = "zips"
    const val USER_THEME = "theme"
    const val USER_REFRESH_TIME = "refreshTime"

    private suspend fun saveString(
        context: Context,
        key: String,
        value: String,
    ) {
        context.datastore.edit { it[stringPreferencesKey(key)] = value }
    }

    private fun getString(
        context: Context,
        key: String,
    ): Flow<String?> {
        return context.datastore.data.map { it[stringPreferencesKey(key)] }
    }

    fun getTheme(context: Context): Flow<Theme> {
        return getString(context = context, key = USER_THEME).map {
            when (it) {
                Theme.FollowSystem.name -> Theme.FollowSystem
                Theme.Dark.name -> Theme.Dark
                else -> Theme.Light
            }
        }
    }

    suspend fun saveTheme(
        context: Context,
        value: Theme,
    ) = saveString(context, USER_THEME, value.name)

    fun getUnits(context: Context): Flow<Units> {
        return getString(context = context, key = USER_UNITS).map {
            when (it) {
                Units.Metric.name -> Units.Metric
                else -> Units.Imperial
            }
        }
    }

    fun getRefreshTime(context: Context): Flow<Int> {
        return getString(context = context, key = USER_REFRESH_TIME).map {
            it?.toInt() ?: 2
        }
    }

    suspend fun saveUnits(
        context: Context,
        value: Units,
    ) = saveString(context, USER_UNITS, value.name)

    fun getZip(context: Context): Flow<String> = getString(context = context, key = USER_ZIP).map { it.orEmpty() }

    suspend fun saveZip(
        context: Context,
        value: String,
    ) = saveString(context, USER_ZIP, value)

    fun getZips(context: Context): Flow<List<String>> {
        return context.datastore.data.map { prefs ->
            prefs[stringSetPreferencesKey(USER_ZIPS)]?.toList() ?: emptyList()
        }
    }

    suspend fun addZip(
        context: Context,
        zip: String,
    ) {
        context.datastore.edit { prefs ->
            val current = prefs[stringSetPreferencesKey(USER_ZIPS)] ?: emptySet()
            prefs[stringSetPreferencesKey(USER_ZIPS)] = current + zip
        }
    }

    suspend fun removeZip(
        context: Context,
        zip: String,
    ) {
        context.datastore.edit { prefs ->
            val current = prefs[stringSetPreferencesKey(USER_ZIPS)] ?: emptySet()
            prefs[stringSetPreferencesKey(USER_ZIPS)] = current - zip
        }
    }

    suspend fun saveRefreshTime(
        context: Context,
        value: Int,
    ) = saveString(context, USER_REFRESH_TIME, value.toString())
}
