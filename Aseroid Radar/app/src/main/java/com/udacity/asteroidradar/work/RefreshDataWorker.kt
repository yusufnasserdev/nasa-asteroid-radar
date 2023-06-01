package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AppDatabase
import com.udacity.asteroidradar.repository.AppRepository
import retrofit2.HttpException


/**
 *
 * RefreshDataWorker is a Worker class that refreshes the data in the database and the images on a daily basis.
 *
 * @param appContext: Context - The application [Context]
 * @param params: WorkerParameters - Parameters to setup the internal state of this worker
 */

class RefreshDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    /**
     * Companion object to define the [WORK_NAME] constant
     */
    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    /**
     * doWork() is the main method of the Worker class. It is called asynchronously on a background thread provided by WorkManager.
     * It refreshes the asteroids data in the database and the images on a daily basis.
     *
     *  @return Result - The result of the work, either [androidx.work.ListenableWorker.Result.Success] or [androidx.work.ListenableWorker.Result.retry]
     */
    override suspend fun doWork(): Result {
        val database = AppDatabase.getInstance(applicationContext)
        val repository = AppRepository(database)

        return try {
            repository.refreshAsteroids()
            repository.refreshDailyImg()
            Result.success()
        } catch (exception: HttpException) {
            Result.retry()
        }
    }

}
