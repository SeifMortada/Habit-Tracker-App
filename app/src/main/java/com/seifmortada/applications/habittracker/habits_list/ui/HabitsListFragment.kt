package com.seifmortada.applications.habittracker.habits_list.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.seifmortada.applications.habittracker.databinding.FragmentHabitsListBinding
import com.seifmortada.applications.habittracker.habits_list.ui.adapters.HabitAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HabitsListFragment : Fragment() {
    private lateinit var binding: FragmentHabitsListBinding
    private val viewModel: HabitsListViewModel by viewModels()
    private lateinit var uncheckedHabitsAdapter: HabitAdapter
    private lateinit var checkedHabitsAdapter: HabitAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHabitsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeState()
    }

    private fun observeState() {
        collectUnCheckedHabits()
        collectCheckedHabits()
    }

    private fun collectCheckedHabits() {
        lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                viewModel.checkedHabits.collect { habits ->
                    checkedHabitsAdapter.updateData(habits)
                }
            }
        }
    }

    private fun collectUnCheckedHabits() {
        lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                viewModel.uncheckedHabits.collect { habits ->
                    uncheckedHabitsAdapter.updateData(habits)
                }
            }
        }
    }

    private fun initViews() {
        uncheckedHabitsAdapter = HabitAdapter(emptyList()) { habit ->
            viewModel.completeHabit(habit)
        }
        checkedHabitsAdapter = HabitAdapter(emptyList()) { habit ->
            viewModel.completeHabit(habit)
        }
        binding.habitsRv.apply {
            adapter = uncheckedHabitsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.checkedHabitsRv.apply {
            adapter = checkedHabitsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}
