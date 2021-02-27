package com.udacity.asteroidradar.api

import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson
import com.udacity.asteroidradar.Asteroid

@JsonClass(generateAdapter = true)
data class RelativeVelocity(
    @Json(name = "kilometers_per_second") val kms: Double,
    @Json(name = "kilometers_per_hour") val kmh: Double,
)

@JsonClass(generateAdapter = true)
data class MissDistance(
    val astronomical: Double,
    val kilometers: Double,
)

@JsonClass(generateAdapter = true)
data class CloseApproachData(
    @Json(name = "close_approach_date") val closeApproachDate: String,
    @Json(name = "relative_velocity") val relativeVelocity: RelativeVelocity,
    @Json(name = "miss_distance") val distanceFromEarth: MissDistance,
)

@JsonClass(generateAdapter = true)
data class EstimatedDimensions(
    @Json(name = "estimated_diameter_min") val minDiameter: Double,
    @Json(name = "estimated_diameter_max") val maxDiameter: Double,
)

@JsonClass(generateAdapter = true)
data class EstimatedDiameter(
    val kilometers: EstimatedDimensions
)

@JsonClass(generateAdapter = true)
data class NetworkAsteroid(
    val id: Long,
    @Json(name = "name") val codename: String,
    @Json(name = "absolute_magnitude_h") val absoluteMagnitude: Double,
    @Json(name = "estimated_diameter") val estimatedDiameter: EstimatedDiameter,
    @Json(name = "close_approach_data") val closeApproachData: List<CloseApproachData>,
    @Json(name = "is_potentially_hazardous_asteroid") val isPotentiallyHazardous: Boolean
)

@JsonClass(generateAdapter = true)
data class AsteroidsJson(
    @Json(name = "element_count") val elementCount: Int,
    @Json(name = "near_earth_objects") val elements: Map<String, List<NetworkAsteroid>>,
)
