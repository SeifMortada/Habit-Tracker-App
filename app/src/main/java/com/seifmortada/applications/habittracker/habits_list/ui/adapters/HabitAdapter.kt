package com.seifmortada.applications.habittracker.habits_list.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.seifmortada.applications.habittracker.core.domain.models.Habit
import com.seifmortada.applications.habittracker.databinding.HabitItemBinding
import com.seifmortada.applications.habittracker.habits_list.utils.TimeUtils

class HabitAdapter(
    private var habits: List<Habit>,
    private val isFilteredAdapter: Boolean = false,
    private val onEditClicked: (habit: Habit) -> Unit,
    private val onDeleteClicked: (habit: Habit) -> Unit,
    private val onHabitChecked: (Habit) -> Unit
) : RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {

    inner class HabitViewHolder(private val binding: HabitItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(habit: Habit) {
            binding.tvHabitTitle.text = habit.title
            binding.tvHabitDetails.text = habit.details ?: "No details"
            binding.cbHabitChecked.isChecked = if (isFilteredAdapter) true else habit.isChecked
            binding.cbHabitChecked.isEnabled = !isFilteredAdapter
            val completedDatesLayout = binding.allCompletedDates
            completedDatesLayout.removeAllViews()


            habit.completedDates.forEach { timestamp ->
                val formattedDate = TimeUtils.formatDate(timestamp)
                val completedDateTextView = TextView(itemView.context).apply {
                    text = "✅ " + formattedDate
                    textSize = 12f
                    setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            android.R.color.darker_gray
                        )
                    )
                }
                completedDatesLayout.addView(completedDateTextView)
            }
            binding.cbHabitChecked.setOnCheckedChangeListener { _, isChecked ->
                onHabitChecked(habit)
            }

            binding.editImg.setOnClickListener {
                onEditClicked(habit)
            }
            binding.deleteImg.setOnClickListener {
                onDeleteClicked(habit)
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
