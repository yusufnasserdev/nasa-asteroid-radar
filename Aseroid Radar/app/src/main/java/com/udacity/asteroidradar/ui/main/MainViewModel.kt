package com.udacity.asteroidradar.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.repository.AppRepository
import kotlinx.coroutines.launch
import timber.log.Timber

enum class Status { LOADING, ERROR, DONE }

enum class Filter { /*Default value*/ SAVED, WEEK, TODAY }

class MainViewModel(private val appRepository: AppRepository) : ViewModel() {

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    var filter = Filter.SAVED
        set(value) {
            field = value
            displayAsteroids(filter)
        }

    val nasaDailyImg = appRepository.dailyImg
    val nasaAsteroids = appRepository.asteroids

    init {
        retrieveDailyImg()
        getAsteroids()
    }

    private fun retrieveDailyImg() {
        viewModelScope.launch {
            try {
                appRepository.refreshDailyImg()
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun getAsteroids() {
        _status.value = Status.LOADING

        viewModelScope.launch {
            try {
                appRepository.refreshAsteroids()
                _status.value = Status.DONE
            } catch (exception: Exception) {
                Timber.e(exception)
                _status.value = Status.ERROR
            }
        }

        displayAsteroids(Filter.SAVED)
    }

    private fun displayAsteroids(filter: Filter) {
        viewModelScope.launch {
            try {
                when (filter) {
                    Filter.SAVED -> appRepository.getAllAsteroids()
                    Filter.WEEK -> appRepository.getWeekAsteroids()
                    Filter.TODAY -> appRepository.getTodayAsteroids()
                }
            } catch (exception: Exception) {
                Timber.e(exception)
            }
        }
    }

}