package com.jessosborn.simpleweather.domain.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jessosborn.simpleweather.domain.db.dao.WeatherSnapshotDao
import com.jessosborn.simpleweather.domain.remote.responses.MainTypeConverter
import com.jessosborn.simpleweather.domain.remote.responses.WeatherSnapshot
import com.jessosborn.simpleweather.domain.remote.responses.WeatherTypeConverter

@Database(entities = [WeatherSnapshot::class], version = 1)
@TypeConverters(MainTypeConverter::class, WeatherTypeConverter::class)
abstract class WeatherSnapshotDb : RoomDatabase() {
	abstract fun weatherSnapshotDao(): WeatherSnapshotDao
}