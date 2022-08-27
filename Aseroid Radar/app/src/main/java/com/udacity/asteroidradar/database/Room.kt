package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.api.getTodayDate

@Dao
interface AsteroidDao {
    @Query("select * from asteroids_table order by close_approach_date")
    fun getAsteroids(): List<DatabaseAsteroid>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DatabaseAsteroid)

    @Query("select * from asteroids_table where close_approach_date = :today")
    suspend fun getTodayAsteroids(today: String): List<DatabaseAsteroid>

    @Query("select * from asteroids_table where close_approach_date between :startDate and :endDate")
    suspend fun getStretchAsteroids(startDate: String, endDate: String): List<DatabaseAsteroid>
}

@Dao
interface DailyPicDao {
    @Query("select * from daily_pic_table where date = :today")
    fun getDailyPic(today: String = getTodayDate()): LiveData<DatabaseDailyPic?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dailyPic: DatabaseDailyPic)
}

@Database(entities = [DatabaseAsteroid::class, DatabaseDailyPic::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val asteroidDao: AsteroidDao
    abstract val dailyPicDao: DailyPicDao

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