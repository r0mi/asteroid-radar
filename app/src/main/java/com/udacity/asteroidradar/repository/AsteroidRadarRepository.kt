package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
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
import java.text.SimpleDateFormat
import java.util.*

enum class AsteroidRadarApiStatus { LOADING, ERROR, DONE }
enum class AsteroidFilter { SHOW_WEEK, SHOW_TODAY, SHOW_SAVED }

class AsteroidRadarRepository(private val database: AsteroidRadarDatabase) {

    private val _status = MutableLiveData<AsteroidRadarApiStatus>()

    val status: LiveData<AsteroidRadarApiStatus>
        get() = _status

    val pictureOfDay: LiveData<PictureOfDay?> =
        Transformations.map(database.asteroidRadarDao.getFirstPictureOfDay()) {
            it?.asDomainModel()
        }

    fun getAsteroids(filter: AsteroidFilter): LiveData<List<Asteroid>> {
        val from: Long
        val to: Long
        when (filter) {
            AsteroidFilter.SHOW_WEEK -> {
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                from = calendar.timeInMillis
                calendar.add(Calendar.DAY_OF_YEAR, Constants.DEFAULT_END_DATE_DAYS)
                to = calendar.timeInMillis
            }
            AsteroidFilter.SHOW_TODAY -> {
                from = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis
                to = from
            }
            AsteroidFilter.SHOW_SAVED -> {
                from = 0
                to = Long.MAX_VALUE
            }
        }
        return Transformations.map(database.asteroidRadarDao.getAsteroids(from, to)) {
            it.asDomainModel()
        }
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
            } catch (e: HttpException) {
                parseError(e.response())?.let {
                    Timber.e("Error getting picture of the day: ${it.message}")
                } ?: Timber.e(e)
            } catch (e: IOException) {
                Timber.e("Error getting picture of the day: ${e.message}")
            }
        }
    }

    suspend fun refreshAsteroids(): String {
        return withContext(Dispatchers.IO) {
            _status.postValue(AsteroidRadarApiStatus.LOADING)
            return@withContext try {
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                val startTime = calendar.time
                calendar.add(Calendar.DAY_OF_YEAR, Constants.DEFAULT_END_DATE_DAYS)
                val endTime = calendar.time
                val dateFormat =
                    SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
                val asteroids = Network.nasa.getAsteroids(
                    dateFormat.format(startTime),
                    dateFormat.format(endTime)
                )
                database.asteroidRadarDao.insertAllAsteroids(*asteroids.asDatabaseModel())
                _status.postValue(AsteroidRadarApiStatus.DONE)
                ""
            } catch (e: HttpException) {
                _status.postValue(AsteroidRadarApiStatus.ERROR)
                parseError(e.response())?.let {
                    Timber.e("Error getting asteroids: ${it.message}")
                } ?: Timber.e(e)
                e.message ?: ""
            } catch (e: IOException) {
                _status.postValue(AsteroidRadarApiStatus.ERROR)
                Timber.e("Error getting asteroids: ${e.message}")
                e.message ?: ""
            }
        }
    }

    suspend fun purgeOldAsteroids() {
        withContext(Dispatchers.IO) {
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            database.asteroidRadarDao.deleteOlderAsteroidsThan(calendar.timeInMillis)
        }
    }
}