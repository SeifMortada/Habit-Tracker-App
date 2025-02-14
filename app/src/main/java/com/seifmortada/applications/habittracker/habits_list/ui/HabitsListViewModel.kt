package com.seifmortada.applications.habittracker.habits_list.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seifmortada.applications.habittracker.core.domain.models.Habit
import com.seifmortada.applications.habittracker.core.domain.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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
    val uncheckedHabits: StateFlow<List<Habit>> =
        habits.map { habits -> habits.filter { !it.isChecked } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val checkedHabits: StateFlow<List<Habit>> =
        habits.map { habits -> habits.filter { it.isChecked } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        insertFakeHabits()
    }
    fun completeHabit(habit: Habit) {
        val updatedHabit = habit.copy(
            isChecked = !habit.isChecked,
            completedDates = if (!habit.isChecked) {
                habit.completedDates + System.currentTimeMillis()
            } else {
                habit.completedDates.dropLast(1)
            }
        )
        viewModelScope.launch {
            habitRepository.upsertHabit(habit)
        }
    }
    fun insertFakeHabits() {
        viewModelScope.launch {
            val fakeHabits = listOf(
                Habit(id=1,"Read a Book", "Read 20 pages", System.currentTimeMillis(), emptyList(),true),
                Habit(id=2,"Exercise", "Do 30 minutes of workout", System.currentTimeMillis(), emptyList(),true),
                Habit(id=3,"Meditation", "Meditate for 10 minutes", System.currentTimeMillis(), emptyList(),true),
                Habit(id=4,"Drink Water", "Drink 8 glasses of water", System.currentTimeMillis(), emptyList(),false),
                Habit(id=5,"id=1,Journal Writing", "Write one page about your day", System.currentTimeMillis(), emptyList(),false)
            )
            fakeHabits.forEach {
                habitRepository.upsertHabit(it)
            }
        }
    }

    companion object {
        private const val StopTimeoutMillis: Long = 5000
        val WhileUiSubscribed: SharingStarted = SharingStarted.WhileSubscribed(StopTimeoutMillis)
    }
}