package com.jessosborn.simpleweather.domain.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.jessosborn.simpleweather.domain.remote.responses.WeatherSnapshot
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherSnapshotDao {
    /**
     * Fetches all snapshots for a specific zip and units combination.
     * This now returns a Flow of a list specific to the location.
     */
    @Query("SELECT * FROM weather_snapshot WHERE zip = :zip AND units = :units ORDER BY dt ASC")
    fun getForecast(
        zip: String,
        units: String,
    ): Flow<List<WeatherSnapshot>>

    /**
     * Inserts a list of snapshots.
     * onConflict = REPLACE means if a snapshot with the same primary key exists, it's replaced.
     * Since we have an auto-generating key, this will always be a fresh insert,
     * which is why we need a specific delete method.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast(weatherData: List<WeatherSnapshot>)

    /**
     * Deletes all snapshots for a specific zip and units combination.
     * This is crucial for replacing old data with fresh data for ONE location.
     */
    @Query("DELETE FROM weather_snapshot WHERE zip = :zip AND units = :units")
    fun deleteForecast(
        zip: String,
        units: String,
    )

    @Transaction
    suspend fun replaceForecast(
        zip: String,
        units: String,
        weatherData: List<WeatherSnapshot>,
    ) {
        deleteForecast(zip, units)
        insertForecast(weatherData)
    }
}
