package com.seifmortada.applications.habittracker.core.domain.repository

import com.seifmortada.applications.habittracker.core.domain.models.Habit
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    suspend fun upsertHabit(habit: Habit)
    fun getAllHabits(): Flow<List<Habit>>
    suspend fun updateCompletion(habitId: Int, completed: List<Long>)
    suspend fun getHabitById(id: Int): Habit
    suspend fun updateHabit(habitId: Int, title: String, details: String)
    suspend fun deleteHabit(habit: Habit)
}