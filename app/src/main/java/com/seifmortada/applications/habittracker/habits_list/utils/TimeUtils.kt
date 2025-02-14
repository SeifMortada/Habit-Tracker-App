package com.seifmortada.applications.habittracker.habits_list.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object TimeUtils {
    fun formatDate(timestamp: Long): String {
        val date = Date(timestamp)
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return format.format(date)
    }
     fun isSameDay(date1: Long, date2: Long): Boolean {
        val calendar1 = Calendar.getInstance().apply { timeInMillis = date1 }
        val calendar2 = Calendar.getInstance().apply { timeInMillis = date2 }
         Log.d("isSameDay", "date1: ${formatDate(date1)}, date2: ${formatDate(date2)}")
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) &&
                calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)
    }
}