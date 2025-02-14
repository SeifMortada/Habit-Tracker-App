package com.seifmortada.applications.habittracker.core.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val details: String?,
    val createdAt: Long,
    val completedDates: List<Long>
)

