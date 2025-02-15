package com.seifmortada.applications.habittracker.add_habit.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seifmortada.applications.habittracker.core.domain.models.Habit
import com.seifmortada.applications.habittracker.core.domain.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddHabitViewModel @Inject constructor(private val habitRepository: HabitRepository) :
    ViewModel() {

    fun upsertHabit(title: String, details: String) {
        viewModelScope.launch {
            val habit = Habit(
                id = null,
                title = title,
                details = details,
                isChecked = false,
                createdAt = System.currentTimeMillis(),
                completedDates = emptyList()
            )
            habitRepository.upsertHabit(habit)
        }
    }
}