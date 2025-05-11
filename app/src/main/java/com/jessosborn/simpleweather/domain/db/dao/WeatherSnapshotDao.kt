package com.jessosborn.simpleweather.domain.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jessosborn.simpleweather.domain.remote.responses.WeatherSnapshot
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherSnapshotDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(currentWeather: List<WeatherSnapshot>)

    @Query("SELECT * FROM weather_snapshot")
    fun getWeather(): Flow<List<WeatherSnapshot>>

    @Delete
    fun delete(currentWeather: List<WeatherSnapshot>)

    @Query("DELETE FROM weather_snapshot")
    fun deleteAll()
}
