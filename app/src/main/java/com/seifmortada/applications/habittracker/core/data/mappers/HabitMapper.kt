package com.seifmortada.applications.habittracker.core.data.mappers

import com.seifmortada.applications.habittracker.core.data.local.HabitEntity
import com.seifmortada.applications.habittracker.core.domain.models.Habit

fun HabitEntity.toHabit(): Habit {
    return Habit(
        title = title,
        details = details,
        createdAt = createdAt,
        completedDates = completedDates
    )
}

fun Habit.toHabitEntity(): HabitEntity {
    return HabitEntity(
        title = title,
        details = details,
        createdAt = createdAt,
        completedDates = completedDates
    )
}