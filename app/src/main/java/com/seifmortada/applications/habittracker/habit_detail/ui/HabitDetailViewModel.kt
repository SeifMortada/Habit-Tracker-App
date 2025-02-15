package com.seifmortada.applications.habittracker.habit_detail.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seifmortada.applications.habittracker.core.domain.models.Habit
import com.seifmortada.applications.habittracker.core.domain.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitDetailViewModel @Inject constructor(private val repository: HabitRepository) :
    ViewModel() {
    private val _habit = MutableStateFlow<Habit?>(null)
    val habit = _habit.asStateFlow()

    fun getHabitById(id: Int) = viewModelScope.launch {
        _habit.value = repository.getHabitById(id)
    }

    fun upsertHabit(title: String, details: String, id: Int) = viewModelScope.launch {
        repository.updateHabit(title = title, details = details, habitId = id)
    }

}

