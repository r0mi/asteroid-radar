package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRadarRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val repository = AsteroidRadarRepository(database)

    private val pictureOfDay = repository.pictureOfDay

    val asteroids = repository.asteroids

    val pictureOfDayUrl: LiveData<String?> = Transformations.map(pictureOfDay) {
        it?.url
    }

    val pictureOfDayTitle: LiveData<String?> = Transformations.map(pictureOfDay) {
        it?.title
    }

    init {
        viewModelScope.launch {
            repository.refreshPictureOfDay()
            repository.refreshAsteroids()
        }
    }
}