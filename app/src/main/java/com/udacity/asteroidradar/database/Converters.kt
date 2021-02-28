package com.udacity.asteroidradar.database

import androidx.room.TypeConverter
import java.util.*

object CalendarConverter {
    @JvmStatic
    @TypeConverter
    fun toCalendar(value: Long): Calendar = Calendar.getInstance().apply {
        timeInMillis = value
    }

    @JvmStatic
    @TypeConverter
    fun fromCalendar(value: Calendar): Long = value.time.time
}