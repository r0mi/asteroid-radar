package com.udacity.asteroidradar.api

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import com.udacity.asteroidradar.Asteroid

class AsteroidAdapter {
    @ToJson
    fun toJson(asteroids: List<Asteroid>): AsteroidsJson {
        throw UnsupportedOperationException()
    }

    @FromJson
    fun fromJson(json: AsteroidsJson): List<Asteroid> {
        val asteroidList = ArrayList<Asteroid>()

        json.elements.toSortedMap().entries.forEach {
            val closeApproachDate = it.key
            it.value.forEach { a ->
                val asteroid = Asteroid(
                    a.id, a.codename, closeApproachDate, a.absoluteMagnitude,
                    a.estimatedDiameter.kilometers.maxDiameter,
                    a.closeApproachData[0].relativeVelocity.kms,
                    a.closeApproachData[0].distanceFromEarth.astronomical,
                    a.isPotentiallyHazardous
                )
                asteroidList.add(asteroid)
            }
        }

        return asteroidList
    }
}