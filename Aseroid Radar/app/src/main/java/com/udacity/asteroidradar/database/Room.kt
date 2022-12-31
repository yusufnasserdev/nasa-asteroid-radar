package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao {
    @Query("select * from asteroids_table order by close_approach_date asc")
    suspend fun getAsteroids(): List<DatabaseAsteroid>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg asteroids: DatabaseAsteroid)

    @Query("select * from asteroids_table where close_approach_date between :startDate and :endDate order by close_approach_date asc")
    suspend fun getStretchAsteroids(startDate: String, endDate: String): List<DatabaseAsteroid>
}

@Dao
interface DailyImageDao {
    @Query("select * from daily_img_table order by date desc limit 1")
    fun getLatestDailyImage(): LiveData<DatabaseDailyImage?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dailyImg: DatabaseDailyImage)
}

@Database(entities = [DatabaseAsteroid::class, DatabaseDailyImage::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val asteroidDao: AsteroidDao
    abstract val dailyImgDao: DailyImageDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {

            synchronized(this) {

                var instance = INSTANCE

                if (instance == null) {
                    instance =
                        Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "app_database"
                        ).fallbackToDestructiveMigration().build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}