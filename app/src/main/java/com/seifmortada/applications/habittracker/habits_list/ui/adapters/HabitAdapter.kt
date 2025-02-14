package com.seifmortada.applications.habittracker.habits_list.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.seifmortada.applications.habittracker.core.domain.models.Habit
import com.seifmortada.applications.habittracker.databinding.HabitItemBinding

class HabitAdapter(
    private var habits: List<Habit>,
    private val onHabitChecked: (Habit) -> Unit
) : RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {

    inner class HabitViewHolder(private val binding: HabitItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(habit: Habit) {
            binding.tvHabitTitle.text = habit.title
            binding.tvHabitDetails.text = habit.details ?: "No details"
            binding.cbHabitChecked.isChecked = habit.isChecked

            binding.cbHabitChecked.setOnCheckedChangeListener { _, isChecked ->
                onHabitChecked(habit.copy(isChecked = isChecked))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val binding = HabitItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HabitViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(habits[position])
    }

    override fun getItemCount(): Int = habits.size

    fun updateData(newHabits: List<Habit>) {
        habits = newHabits
        notifyDataSetChanged()
    }
}
