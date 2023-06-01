package com.udacity.asteroidradar

import android.app.Application
import androidx.work.*
import com.udacity.asteroidradar.work.RefreshDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import timber.log.Timber

/**
 * RadarApp is the application class for the app.
 *
 * It is used to setup the recurring work for the app.
 */

class RadarApp : Application() {

    /**
     * onCreate() is called when the application is starting, before any other application objects have been created.
     */
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree()) // Setup Timber for logging
        delayedInit()
    }

    /**
     * applicationScope is the [CoroutineScope] for the application.
     * It is used to launch a new coroutine on the background thread.
     */

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    /**
     * delayedInit() is used to setup the recurring work for the app.
     *
     * it is delayed to avoid blocking the main thread.
     */

    private fun delayedInit() {
        applicationScope.launch {
            setupRecurringWork()
        }
    }

    /**
     * setupRecurringWork() sets up the recurring work for the app.
     *
     * It sets up the work to refresh the asteroids data in the database and the images
     * on a daily basis. uses the [WorkManager] to enqueue the work and the [Constraints] to
     * set the constraints for the work. The constraints are:
     * 1. The network type should be unmetered
     * 2. The battery should not be low
     * 3. The device should be charging
     * 4. The device should be idle
     */

    private fun setupRecurringWork() {
        // Setup the constraints for the work
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .apply {
                setRequiresDeviceIdle(true)
            }.build()

        // Setup the work to refresh the asteroids data in the database and the images on a daily basis
        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        // Enqueue the work
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            RefreshDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }
}