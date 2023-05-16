package com.udacity.asteroidradar.util

object Constants {
    const val DEFAULT_END_DATE_DAYS = 7
    const val BASE_URL = "https://api.nasa.gov/"
    val API_KEY = System.getenv("API_KEY") ?: "DEMO_KEY"
}