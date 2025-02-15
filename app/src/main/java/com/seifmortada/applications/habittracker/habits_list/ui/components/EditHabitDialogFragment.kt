package com.seifmortada.applications.habittracker.habits_list.ui.components

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.seifmortada.applications.habittracker.core.domain.models.Habit
import com.seifmortada.applications.habittracker.databinding.DialogEditHabitBinding

class EditHabitDialogFragment(
    private val habit: Habit,
    private val onHabitUpdated: (Habit) -> Unit
) : DialogFragment() {
    private lateinit var binding: DialogEditHabitBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogEditHabitBinding.inflate(LayoutInflater.from(requireContext()))
        binding.editHabitTitle.setText(habit.title)
        binding.editHabitDescription.setText(habit.details)

        return AlertDialog.Builder(requireContext())
            .setTitle("Edit Habit")
            .setView(binding.root)
            .setPositiveButton("OK") { _, _ ->
                val updatedHabit = habit.copy(
                    title = binding.editHabitTitle.text.toString(),
                    details = binding.editHabitDescription.text.toString()
                )
                onHabitUpdated(updatedHabit)
            }
            .setNegativeButton("Cancel", null)
            .create()
    }
}
