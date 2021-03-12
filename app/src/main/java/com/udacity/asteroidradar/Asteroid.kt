package com.udacity.asteroidradar

import android.os.Parcelable
import com.udacity.asteroidradar.database.DatabaseAsteroid
import kotlinx.parcelize.Parcelize
import java.text.ParseException
import java.util.*

@Parcelize
data class Asteroid(
    val id: Long, val codename: String, val closeApproachDate: String,
    val absoluteMagnitude: Double, val estimatedDiameter: Double,
    val relativeVelocity: Double, val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
) : Parcelable

fun List<Asteroid>.asDatabaseModel(): Array<DatabaseAsteroid> {
    return map {
        val calendar = Calendar.getInstance()
        val date = try {
            it.closeApproachDate.toDate(Constants.API_QUERY_DATE_FORMAT) ?: calendar.time
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