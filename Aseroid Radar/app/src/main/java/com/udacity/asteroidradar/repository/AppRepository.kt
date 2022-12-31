package com.udacity.asteroidradar.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.database.*
import com.udacity.asteroidradar.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AppRepository(private val database: AppDatabase) {


    val dailyImg = Transformations.map(database.dailyImgDao.getLatestDailyImage()) {
        it?.asDailyImageDomainModel()
    }

    private var _asteroids = MutableLiveData<List<DatabaseAsteroid>>()
    val asteroids
        get() = Transformations.map(_asteroids) {
            it.asAsteroidDomainModel()
        }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val startDate = getTodayDate()
            val endDate = getDate(7)
            val asteroids = Network.service.getAsteroids(Constants.API_KEY, startDate, endDate)
            val asteroidsJson = JSONObject(asteroids)
            val resultAsteroids = parseAsteroidsJsonResult(asteroidsJson).toTypedArray()
            database.asteroidDao.insertAll(*resultAsteroids)
        }
    }

    suspend fun refreshDailyImg() {
        withContext(Dispatchers.IO) {
            val dailyImg = Network.service.getDailyImg(Constants.API_KEY)
            database.dailyImgDao.insert(
                DatabaseDailyImage(
                    date = dailyImg.date,
                    url = dailyImg.url,
                    mediaType = dailyImg.mediaType,
                    title = dailyImg.title
                )
            )
        }
    }

    suspend fun getAllAsteroids() {
        _asteroids.value = database.asteroidDao.getAsteroids()
    }

    suspend fun getWeekAsteroids() {
        val startDate = getTodayDate()
        val endDate = getDate(6)
        _asteroids.value = database.asteroidDao.getStretchAsteroids(startDate, endDate)
    }

    suspend fun getTodayAsteroids() {
        _asteroids.value = database.asteroidDao.getStretchAsteroids(getTodayDate(), getTodayDate())
    }

}