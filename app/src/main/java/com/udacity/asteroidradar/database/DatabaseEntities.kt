package com.udacity.asteroidradar.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "picture_of_day")
data class DatabasePictureOfDay constructor(
    @PrimaryKey
    val url: String,
    val title: String
)

@Entity(tableName = "asteroid")
data class DatabaseAsteroid constructor(
    @PrimaryKey
    val id: Long,
    val name: String,
    @ColumnInfo(name = "close_approach_date")
    val closeApproachDate: Calendar,
    @ColumnInfo(name = "absolute_magnitude")
    val absoluteMagnitude: Double,
    @ColumnInfo(name = "estimated_diameter")
    val estimatedDiameter: Double,
    @ColumnInfo(name = "relative_velocity")
    val relativeVelocity: Double,
    @ColumnInfo(name = "distance_from_earth")
    val distanceFromEarth: Double,
    @ColumnInfo(name = "is_potentially_hazardous")
    val isPotentiallyHazardous: Boolean
)

fun DatabasePictureOfDay.asDomainModel() = PictureOfDay("image", title, url)

fun List<DatabaseAsteroid>.asDomainModel() = map {
    Asteroid(
        id = it.id,
        codename = it.name,
        closeApproachDate = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault()).format(it.closeApproachDate.timeInMillis),
        absoluteMagnitude = it.absoluteMagnitude,
        estimatedDiameter = it.estimatedDiameter,
        relativeVelocity = it.relativeVelocity,
        distanceFromEarth = it.distanceFromEarth,
        isPotentiallyHazardous = it.isPotentiallyHazardous
    )
}
