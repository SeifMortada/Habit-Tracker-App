package com.seifmortada.applications.habittracker.habits_list.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.seifmortada.applications.habittracker.core.domain.models.Habit
import com.seifmortada.applications.habittracker.databinding.HabitItemBinding

class CheckedHabitAdapter(
    private var checkedHabits: MutableList<Habit>
) : RecyclerView.Adapter<CheckedHabitAdapter.CheckedHabitViewHolder>() {

    inner class CheckedHabitViewHolder(private val binding: HabitItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(habit: Habit) {
            binding.tvHabitTitle.text = habit.title
            binding.tvHabitDetails.text = habit.details ?: "No details"
            binding.cbHabitChecked.isChecked = true
            binding.cbHabitChecked.isEnabled = false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckedHabitViewHolder {
        val binding = HabitItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CheckedHabitViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CheckedHabitViewHolder, position: Int) {
        holder.bind(checkedHabits[position])
    }

    override fun getItemCount(): Int = checkedHabits.size

    fun addHabit(habit: Habit) {
        checkedHabits.add(habit)
        notifyDataSetChanged()
    }
}
