package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.api.getTodayDate
import com.udacity.asteroidradar.api.getWeekendDate
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.*
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.DailyPic
import com.udacity.asteroidradar.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject

class AppRepository(private val database: AppDatabase) {


    val dailyPic = Transformations.map(database.dailyPicDao.getDailyPic()) {
        it?.asDailyPicDomainModel()
    }

    private var _asteroids = MutableLiveData<List<DatabaseAsteroid>>()
    val asteroids
        get() = Transformations.map(_asteroids) {
            it.asAsteroidDomainModel()
        }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val startDate = getTodayDate()
            val endDate = getWeekendDate()
            val asteroids = Network.service.getAsteroids(Constants.API_KEY, startDate, endDate)
            val asteroidsJson = JSONObject(asteroids)
            val resultAsteroids = parseAsteroidsJsonResult(asteroidsJson).toTypedArray()
            database.asteroidDao.insertAll(*resultAsteroids)
        }
    }

    suspend fun refreshDailyPic() {
        withContext(Dispatchers.IO) {
            val dailyPic = Network.service.getDailyPic(Constants.API_KEY)
            database.dailyPicDao.insert(
                DatabaseDailyPic(
                    date = getTodayDate(),
                    url = dailyPic.url,
                    mediaType = dailyPic.mediaType,
                    title = dailyPic.title
                )
            )
        }
    }

    suspend fun getAllAsteroids() {
        withContext(Dispatchers.IO) {
            _asteroids.postValue(database.asteroidDao.getAsteroids())
        }
    }

    suspend fun getWeekAsteroids() {
        val startDate = getTodayDate()
        val endDate = getWeekendDate()
        _asteroids.value = database.asteroidDao.getStretchAsteroids(startDate, endDate)

    }

    suspend fun getTodayAsteroids() {
        _asteroids.value = database.asteroidDao.getTodayAsteroids(getTodayDate())
    }

}