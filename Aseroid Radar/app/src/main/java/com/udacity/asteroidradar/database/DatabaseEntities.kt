package com.udacity.asteroidradar.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "asteroids_table")
data class DatabaseAsteroid constructor(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "code_name")
    val codename: String,
    @ColumnInfo(name = "close_approach_date")
    val closeApproachDate: String,
    @ColumnInfo(name = "absolute_magnitude")
    val absoluteMagnitude: Double,
    @ColumnInfo(name = "estimated_diameter")
    val estimatedDiameter: Double,
    @ColumnInfo(name = "relative_velocity")
    val relativeVelocity: Double,
    @ColumnInfo(name = "distance_from_earth")
    val distanceFromEarth: Double,
    @ColumnInfo(name = "is_potentially_hazardous")
    val isPotentiallyHazardous: Boolean
)

@Entity(tableName = "daily_img_table")
data class DatabaseDailyImage constructor(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "url")
    val url: String,
    @ColumnInfo(name = "media_type")
    val mediaType: String,
    @ColumnInfo(name = "title")
    val title: String
)