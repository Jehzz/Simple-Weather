package com.jessosborn.simpleweather.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.jessosborn.simpleweather.domain.Units
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

val Context.datastore: DataStore<Preferences> by preferencesDataStore("settings")

object DataStoreUtil {

    const val USER_UNITS = "units"
    const val USER_ZIP = "zip"

    suspend fun saveString(context: Context, key: String, value: String) {
        context.datastore.edit {
            it[stringPreferencesKey(key)] = value
        }
    }

    suspend fun getString(context: Context, key: String): String? {
        return context.datastore.data.map {
            it[stringPreferencesKey(key)]
        }.firstOrNull()
    }

    suspend fun getUnits(context: Context): Units {
        return when (getString(context = context, key = USER_UNITS)) {
            Units.Metric.name -> Units.Metric
            Units.Imperial.name -> Units.Imperial
            else -> {
                Units.Imperial
            }
        }
    }

    suspend fun saveUnits(context: Context, value: Units) {
        context.datastore.edit {
            it[stringPreferencesKey(USER_UNITS)] = value.name
        }
    }
}
