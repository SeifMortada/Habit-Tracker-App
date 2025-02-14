package com.seifmortada.applications.habittracker.core.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromListLong(value: List<Long>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toListLong(value: String): List<Long> {
        val type = object : TypeToken<List<Long>>() {}.type
        return gson.fromJson(value, type) ?: emptyList()
    }
}