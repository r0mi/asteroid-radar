package com.udacity.asteroidradar.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class APIErrorWrapper(val error: APIError)

@JsonClass(generateAdapter = true)
data class APIError(val code: String, var message: String)
