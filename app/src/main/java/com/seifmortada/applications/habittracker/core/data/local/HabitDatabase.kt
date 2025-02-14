package com.seifmortada.applications.habittracker.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [HabitEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class HabitDatabase : RoomDatabase() {
    abstract val habitDao: HabitDao
}