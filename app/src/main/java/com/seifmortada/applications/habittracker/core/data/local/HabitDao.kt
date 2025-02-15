package com.seifmortada.applications.habittracker.core.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Upsert
    suspend fun insertHabit(habit: HabitEntity)

    @Query("SELECT * FROM habits ORDER BY createdAt DESC")
    fun getAllHabits(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun getHabitById(id: Int): HabitEntity

    @Query("UPDATE habits SET title = :title, details = :details WHERE id = :habitId")
    suspend fun updateHabit(habitId: Int, title: String, details: String)

    @Query("UPDATE habits SET completedDates = :completed WHERE id = :habitId")
    suspend fun updateCompletion(habitId: Int, completed: List<Long>)

    @Delete
    suspend fun deleteHabit(habit: HabitEntity)
}
