package com.seifmortada.applications.habittracker.core.data.repository

import com.seifmortada.applications.habittracker.core.data.local.HabitDao
import com.seifmortada.applications.habittracker.core.data.mappers.toHabit
import com.seifmortada.applications.habittracker.core.data.mappers.toHabitEntity
import com.seifmortada.applications.habittracker.core.domain.models.Habit
import com.seifmortada.applications.habittracker.core.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HabitRepositoryImpl @Inject constructor(private val habitDao: HabitDao) : HabitRepository {
    override suspend fun upsertHabit(habit: Habit) {
        habitDao.insertHabit(habit.toHabitEntity())
    }

    override fun getAllHabits(): Flow<List<Habit>> {
        return habitDao.getAllHabits().map { habits ->
            habits.map { it.toHabit() }
        }
    }

    override suspend fun updateCompletion(habitId: Int, completed: List<Long>) {
        habitDao.updateCompletion(habitId, completed)
    }

    override suspend fun getHabitById(id: Int): Habit {
        return habitDao.getHabitById(id).toHabit()
    }

    override suspend fun updateHabit(habitId: Int, title: String, details: String) {
        habitDao.updateHabit(habitId, title, details)
    }

    override suspend fun deleteHabit(habit: Habit) {
        habitDao.deleteHabit(habit.toHabitEntity())
    }
}