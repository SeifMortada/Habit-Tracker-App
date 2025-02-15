package com.seifmortada.applications.habittracker.habits_list.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seifmortada.applications.habittracker.core.domain.models.Habit
import com.seifmortada.applications.habittracker.core.domain.repository.HabitRepository
import com.seifmortada.applications.habittracker.habits_list.utils.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitsListViewModel @Inject constructor(private val habitRepository: HabitRepository) :
    ViewModel() {
    private val habits: StateFlow<List<Habit>> =
        habitRepository.getAllHabits()
            .stateIn(
                scope = viewModelScope,
                started = WhileUiSubscribed,
                initialValue = emptyList()
            )

    val todayTimestamp = System.currentTimeMillis()

    val uncheckedHabits: StateFlow<List<Habit>> =
        habits.map { habits ->
            habits.filter { habit ->
                !habit.isChecked && TimeUtils.isSameDay(habit.createdAt, todayTimestamp)
            }
        }.stateIn(viewModelScope, WhileUiSubscribed, emptyList())

    val checkedHabits: StateFlow<List<Habit>> =
        habits.map { habits ->
            habits.filter { habit ->
                habit.isChecked && habit.completedDates.any { completedDate ->
                    TimeUtils.isSameDay(completedDate, todayTimestamp)
                }
            }
        }.stateIn(viewModelScope, WhileUiSubscribed, emptyList())

    private val _selectedDate = MutableStateFlow<Long?>(null)
    val filteredHabits = _selectedDate
        .combine(habits) { date, habitList ->
            val filteredList = date?.let { d ->
                habitList.filter { habit ->
                    habit.completedDates.any { TimeUtils.isSameDay(it, d) }
                }
            } ?: emptyList()
            filteredList
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    fun toggleHabitCompletion(habit: Habit) = viewModelScope.launch {
        habitRepository.upsertHabit(
            habit.copy(
                isChecked = !habit.isChecked,
                completedDates = if (!habit.isChecked) {
                    habit.completedDates + System.currentTimeMillis()
                } else {
                    habit.completedDates.dropLast(1)
                }
            )
        )
    }

    fun getHabitsByDate(date: Long) {
        _selectedDate.value = date
    }

    fun upsertHabit(updatedHabit: Habit) {
        viewModelScope.launch {
            habitRepository.upsertHabit(updatedHabit)
        }
    }

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch {
            habitRepository.deleteHabit(habit)
        }
    }

    companion object {
        private const val StopTimeoutMillis: Long = 5000
        val WhileUiSubscribed: SharingStarted = SharingStarted.WhileSubscribed(StopTimeoutMillis)
    }
}