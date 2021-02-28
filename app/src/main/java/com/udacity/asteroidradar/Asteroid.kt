package com.udacity.asteroidradar

import android.os.Parcelable
import com.udacity.asteroidradar.database.DatabaseAsteroid
import kotlinx.parcelize.Parcelize
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Parcelize
data class Asteroid(val id: Long, val codename: String, val closeApproachDate: String,
                    val absoluteMagnitude: Double, val estimatedDiameter: Double,
                    val relativeVelocity: Double, val distanceFromEarth: Double,
                    val isPotentiallyHazardous: Boolean) : Parcelable

fun List<Asteroid>.asDatabaseModel(): Array<DatabaseAsteroid> {
    return map {
        val calendar = Calendar.getInstance()
        val date = try {
            SimpleDateFormat(
                Constants.API_QUERY_DATE_FORMAT,
                Locale.getDefault()
            ).parse(it.closeApproachDate) ?: calendar.time
        } catch (e: ParseException) {
            calendar.time
        }
        calendar.time = date
        DatabaseAsteroid(
            id = it.id,
            name = it.codename,
            closeApproachDate = calendar,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()
}