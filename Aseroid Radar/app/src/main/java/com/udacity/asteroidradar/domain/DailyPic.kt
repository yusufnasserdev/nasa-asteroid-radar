package com.udacity.asteroidradar.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.util.*


data class DailyPic(
    @Json(name = "media_type")
    val mediaType: String,
    val title: String,
    val url: String
)