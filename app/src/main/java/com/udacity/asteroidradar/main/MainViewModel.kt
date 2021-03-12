package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidFilter
import com.udacity.asteroidradar.repository.AsteroidRadarRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val repository = AsteroidRadarRepository(database)

    val pictureOfDay = repository.pictureOfDay

    private val _filter = MutableLiveData<AsteroidFilter>()

    val asteroids = Transformations.switchMap(_filter) { filter ->
        repository.getAsteroids(filter)
    }

    val status = repository.status

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid?>()

    val navigateToSelectedAsteroid: LiveData<Asteroid?>
        get() = _navigateToSelectedAsteroid

    init {
        updateFilter(AsteroidFilter.SHOW_WEEK)
        viewModelScope.launch {
            repository.refreshPictureOfDay()
            repository.refreshAsteroids()
        }
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun updateFilter(filter: AsteroidFilter) {
        _filter.value = filter
    }
}