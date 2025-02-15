package com.seifmortada.applications.habittracker.habits_list.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.seifmortada.applications.habittracker.R
import com.seifmortada.applications.habittracker.core.base.BaseFragment
import com.seifmortada.applications.habittracker.core.domain.models.Habit
import com.seifmortada.applications.habittracker.core.ui.extensions.collectFlow
import com.seifmortada.applications.habittracker.databinding.FragmentHabitsListBinding
import com.seifmortada.applications.habittracker.habits_list.ui.adapters.HabitAdapter
import com.seifmortada.applications.habittracker.habits_list.utils.TimeUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HabitsListFragment : BaseFragment<FragmentHabitsListBinding, HabitsListViewModel>() {
    override val viewModel: HabitsListViewModel by viewModels()
    private lateinit var uncheckedHabitsAdapter: HabitAdapter
    private lateinit var checkedHabitsAdapter: HabitAdapter
    private lateinit var filteredHabitsAdapter: HabitAdapter

    private var isFiltered: Boolean = false
    override fun initializeViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHabitsListBinding {
        return FragmentHabitsListBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeState()
    }

    private fun handleFilteredHabits(filteredHabits: List<Habit>) {
        toggleHabitVisibility(filteredHabits.isNotEmpty())
        filteredHabitsAdapter.updateData(filteredHabits)
        isFiltered = false
    }


    private fun observeState() {
        collectFlow(viewModel.checkedHabits) { checkedHabits ->
            checkedHabitsAdapter.updateData(checkedHabits)
        }
        collectFlow(viewModel.uncheckedHabits) { uncheckedHabits ->
            uncheckedHabitsAdapter.updateData(uncheckedHabits)
        }
        collectFlow(viewModel.filteredHabits) { handleFilteredHabits(it) }
    }


    private fun createHabitAdapter(isFilteredAdapter: Boolean = false) =
        HabitAdapter(
            emptyList(),
            isFilteredAdapter = isFilteredAdapter,
            onEditClicked = { habit ->
                navigateToHabitDetails(habit)
            },
            onDeleteClicked = {
                handleHabitDeletion(it)
            },
            onHabitChecked = { habit ->
                viewModel.toggleHabitCompletion(habit)
            }
        )

    private fun handleHabitDeletion(deletedHabit: Habit) {
        viewModel.deleteHabit(deletedHabit)
        Snackbar.make(binding.root, "Habit deleted", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                viewModel.upsertHabit(deletedHabit)
            }.show()
    }

    private fun navigateToHabitDetails(habit: Habit) {
        val action =
            HabitsListFragmentDirections.actionHabitsListFragmentToHabitDetailFragment(
                habit.id!!
            )
        findNavController().navigate(action)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, adapter: HabitAdapter) {
        recyclerView.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initViews() {
        uncheckedHabitsAdapter = createHabitAdapter()
        checkedHabitsAdapter = createHabitAdapter()
        filteredHabitsAdapter = createHabitAdapter(isFilteredAdapter = true)
        setupRecyclerView(binding.habitsRv, uncheckedHabitsAdapter)
        setupRecyclerView(binding.checkedHabitsRv, checkedHabitsAdapter)
        setupRecyclerView(binding.filteredHabitsRv, filteredHabitsAdapter)

        binding.datePickerText.setOnClickListener {
            showDatePicker()
        }
        binding.addBtn.setOnClickListener {
            findNavController().navigate(R.id.action_habitsListFragment_to_addHabitFragment)
        }
    }

    private fun toggleHabitVisibility(isFiltered: Boolean) {
        with(binding) {
            val allHabitsVisible = if (isFiltered) View.GONE else View.VISIBLE
            val filteredVisible = if (isFiltered) View.VISIBLE else View.GONE

            habitsTxt.visibility = allHabitsVisible
            habitsRv.visibility = allHabitsVisible
            checkedHabitsRv.visibility = allHabitsVisible
            checkedHabitsTxt.visibility = allHabitsVisible
            filteredHabitsRv.visibility = filteredVisible
            filteredHabitsTxt.visibility = filteredVisible
        }
    }

    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker().build()
        datePicker.show(childFragmentManager, "DATE_PICKER")

        datePicker.addOnPositiveButtonClickListener { selectedDate ->
            val formattedDate = TimeUtils.formatDate(selectedDate)
            binding.datePickerText.text = formattedDate
            viewModel.getHabitsByDate(selectedDate)
            isFiltered = true
        }
    }
}
