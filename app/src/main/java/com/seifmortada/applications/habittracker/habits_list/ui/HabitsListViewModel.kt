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
        viewModelScope.launch {
            val updatedHabit = habit.copy(
                isChecked = !habit.isChecked,
                completedDates = if (!habit.isChecked) {
                    habit.completedDates + System.currentTimeMillis()
                } else {
                    habit.completedDates.dropLast(1)
                }
            )
            habitRepository.upsertHabit(updatedHabit)
        }
    }

    private fun insertFakeHabits() {
        viewModelScope.launch {
            val fakeHabits = listOf(
                Habit(
                    id = 1,
                    title = "Read a Book",
                    details = "Read 20 pages",
                    createdAt = System.currentTimeMillis(),
                    completedDates = listOf(
                        System.currentTimeMillis() - 1000000000L, // Example date 1
                        System.currentTimeMillis() - 2000000000L  // Example date 2
                    ),
                    isChecked = true
                ),
                Habit(
                    id = 2,
                    title = "Exercise",
                    details = "Do 30 minutes of workout",
                    createdAt = System.currentTimeMillis(),
                    completedDates = listOf(
                        System.currentTimeMillis() - 3000000000L, // Example date 1
                        System.currentTimeMillis() - 4000000000L  // Example date 2
                    ),
                    isChecked = true
                ),
                Habit(
                    id = 3,
                    title = "Meditation",
                    details = "Meditate for 10 minutes",
                    createdAt = System.currentTimeMillis(),
                    completedDates = listOf(
                        System.currentTimeMillis() - 5000000000L, // Example date 1
                        System.currentTimeMillis() - 6000000000L  // Example date 2
                    ),
                    isChecked = true
                ),
                Habit(
                    id = 4,
                    title = "Drink Water",
                    details = "Drink 8 glasses of water",
                    createdAt = System.currentTimeMillis(),
                    completedDates = listOf(
                        System.currentTimeMillis() - 7000000000L  // Example date 1
                    ),
                    isChecked = false
                ),
                Habit(
                    id = 5,
                    title = "Journal Writing",
                    details = "Write one page about your day",
                    createdAt = System.currentTimeMillis(),
                    completedDates = listOf(
                        System.currentTimeMillis() - 8000000000L  // Example date 1
                    ),
                    isChecked = false
                )
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