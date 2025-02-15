package com.seifmortada.applications.habittracker.habit_detail.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.seifmortada.applications.habittracker.R
import com.seifmortada.applications.habittracker.core.base.BaseFragment
import com.seifmortada.applications.habittracker.core.ui.extensions.collectFlow
import com.seifmortada.applications.habittracker.databinding.FragmentHabitDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HabitDetailFragment : BaseFragment<FragmentHabitDetailBinding, HabitDetailViewModel>() {
    private val args = navArgs<HabitDetailFragmentArgs>()
    override val viewModel: HabitDetailViewModel by viewModels()
    override fun initializeViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHabitDetailBinding {
        return FragmentHabitDetailBinding.inflate(inflater, container, false)
    }

    private fun fieldsAreCorrect(): Boolean {
        val title = binding.editHabitTitle.text?.trim().toString()
        val description = binding.editHabitDescription.text?.trim().toString()
        return !(title.isEmpty() || description.isEmpty())
    }

    private fun onSaveClicked() {
        if (fieldsAreCorrect()) {
            viewModel.upsertHabit(
                title = binding.editHabitTitle.text?.trim().toString(),
                details = binding.editHabitDescription.text?.trim().toString(),
                id = args.value.habitId
            )
            findNavController().navigateUp()
        } else {
            showToastMessage("Please fill all fields")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getHabitById(args.value.habitId)
        observeState()
        binding.btnSaveHabit.setOnClickListener {
            onSaveClicked()
        }
    }

    private fun observeState() {
        collectFlow(viewModel.habit) {
            if (it != null) {
                binding.editHabitTitle.setText(it.title)
                binding.editHabitDescription.setText(it.details)
            }
        }
    }
}