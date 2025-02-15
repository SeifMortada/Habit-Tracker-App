package com.seifmortada.applications.habittracker.habits_list.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import com.seifmortada.applications.habittracker.core.domain.models.Habit
import com.seifmortada.applications.habittracker.core.ui.extensions.collectFlow
import com.seifmortada.applications.habittracker.databinding.FragmentHabitsListBinding
import com.seifmortada.applications.habittracker.habits_list.ui.adapters.HabitAdapter
import com.seifmortada.applications.habittracker.habits_list.ui.components.EditHabitDialogFragment
import com.seifmortada.applications.habittracker.habits_list.utils.TimeUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HabitsListFragment : Fragment() {
    private var _binding: FragmentHabitsListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HabitsListViewModel by viewModels()
    private lateinit var uncheckedHabitsAdapter: HabitAdapter
    private lateinit var checkedHabitsAdapter: HabitAdapter
    private lateinit var filteredHabitsAdapter: HabitAdapter
    private var isFiltered: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHabitsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeState()
    }

    private fun observeState() {
        collectFlow(viewModel.checkedHabits) { checkedHabitsAdapter.updateData(it) }
        collectFlow(viewModel.uncheckedHabits) { uncheckedHabitsAdapter.updateData(it) }
        collectFlow(viewModel.filteredHabits) { habits ->
            toggleHabitVisibility(habits.isNotEmpty())
            if (habits.isEmpty() && isFiltered) {
                Toast.makeText(
                    requireContext(),
                    "No habits found for the selected date",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                filteredHabitsAdapter.updateData(habits)
                isFiltered = false
            }
        }
    }

    private fun createHabitAdapter(isFilteredAdapter: Boolean = false) =
        HabitAdapter(
            emptyList(),
            isFilteredAdapter = isFilteredAdapter,
            onHabitChecked = { habit -> showEditHabitDialog(habit) },
            onHabitClicked = { habit -> viewModel.completeHabit(habit) }
        )

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

    private fun showEditHabitDialog(habit: Habit) {
        EditHabitDialogFragment(habit) { updatedHabit ->
            viewModel.upsertHabit(updatedHabit)
        }.show(childFragmentManager, "EditHabitDialog")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
