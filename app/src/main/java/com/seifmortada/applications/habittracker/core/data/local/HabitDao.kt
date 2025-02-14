package com.seifmortada.applications.habittracker.core.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Upsert
    suspend fun insertHabit(habit: HabitEntity)

    @Query("SELECT * FROM habits ORDER BY createdAt DESC")
    fun getAllHabits(): Flow<List<HabitEntity>>

    @Query("UPDATE habits SET completedDates = :completed WHERE id = :habitId")
    suspend fun updateCompletion(habitId: Int, completed: List<Long>)
}
