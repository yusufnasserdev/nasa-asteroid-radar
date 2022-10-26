package com.udacity.asteroidradar.database

import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.DailyImage

fun List<DatabaseAsteroid>.asAsteroidDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}

fun DatabaseDailyImage.asDailyImageDomainModel(): DailyImage {
    return DailyImage(
        date = this.date,
        mediaType = this.mediaType,
        title = this.title,
        url = this.url
    )
}