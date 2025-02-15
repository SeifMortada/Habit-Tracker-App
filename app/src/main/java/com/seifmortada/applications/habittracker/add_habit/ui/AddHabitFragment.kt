package com.seifmortada.applications.habittracker.add_habit.ui

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.seifmortada.applications.habittracker.R
import com.seifmortada.applications.habittracker.core.base.BaseFragment
import com.seifmortada.applications.habittracker.databinding.FragmentAddHabitBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddHabitFragment : BaseFragment<FragmentAddHabitBinding, AddHabitViewModel>() {
    override val viewModel: AddHabitViewModel by viewModels()
    override fun initializeViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddHabitBinding {
        return FragmentAddHabitBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSaveHabit.setOnClickListener {
            saveHabit()
        }
    }

    private fun saveHabit() {
        if (areFieldsValid()) {
            viewModel.upsertHabit(
                title = binding.addHabitTitle.text.toString().trim(),
                details = binding.addHabitDescription.text.toString().trim()
            )
            showToastMessage("Habit saved successfully")
            findNavController().navigateUp()
        } else
            showToastMessage("Please fill all fields")
    }

    private fun areFieldsValid(): Boolean {
        val title = binding.addHabitTitle.text.toString().trim()
        val description = binding.addHabitDescription.text.toString().trim()
        return title.isNotEmpty() && description.isNotEmpty()
    }
}