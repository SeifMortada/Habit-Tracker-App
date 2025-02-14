package com.seifmortada.applications.habittracker.habits_list.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TimeFormater {
    fun formatDate(timestamp: Long): String {
        val date = Date(timestamp)
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return format.format(date)
    }
}