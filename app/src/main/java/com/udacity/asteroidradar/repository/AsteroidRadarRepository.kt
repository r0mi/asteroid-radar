package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.api.parseError
import com.udacity.asteroidradar.asDatabaseModel
import com.udacity.asteroidradar.database.AsteroidRadarDatabase
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

class AsteroidRadarRepository(private val database: AsteroidRadarDatabase) {

    val pictureOfDay: LiveData<PictureOfDay?> =
        Transformations.map(database.asteroidRadarDao.getFirstPictureOfDay()) {
            it?.asDomainModel()
        }

    suspend fun refreshPictureOfDay() {
        withContext(Dispatchers.IO) {
            try {
                val picture = Network.nasa.getPictureOfDay()
                if (picture.mediaType == "image") {
                    val currentPictureOfDayExists =
                        database.asteroidRadarDao.pictureOfDayExists(picture.url)
                    if (!currentPictureOfDayExists) {
                        database.asteroidRadarDao.deletePicturesOfDay()
                        database.asteroidRadarDao.insertPictureOfDay(picture.asDatabaseModel())
                    }
                }
                Timber.e("pic of the day: $picture")
            } catch (e: HttpException) {
                parseError(e.response())?.let {
                    Timber.e("Error getting picture of the day: ${it.message}")
                } ?: Timber.e(e)
            } catch (e: IOException) {
                Timber.e("Error getting picture of the day: ${e.message}")
            }
        }
    }
}