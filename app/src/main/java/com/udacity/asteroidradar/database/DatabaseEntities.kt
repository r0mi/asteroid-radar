package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.PictureOfDay

@Entity
data class DatabasePictureOfDay constructor(
    @PrimaryKey
    val url: String,
    val title: String
)

fun DatabasePictureOfDay.asDomainModel() = PictureOfDay("image", title, url)

