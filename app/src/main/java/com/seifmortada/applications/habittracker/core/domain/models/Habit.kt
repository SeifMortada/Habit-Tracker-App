package com.seifmortada.applications.habittracker.core.domain.models

data class Habit(
    val id: Int?,
    val title: String,
    val details: String?,
    val createdAt: Long,
    val completedDates: List<Long>,
    val isChecked: Boolean
)
