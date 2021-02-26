package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import timber.log.Timber

/**
 * A retrofit service to fetch a picture of the day and asteroid list.
 */
interface NASAService {
    @GET("planetary/apod")
    suspend fun getPictureOfDay(): PictureOfDay
}

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Main entry point for network access.
 */
object Network {
    // Configure retrofit to parse JSON and use coroutines
    val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(OkHttpClient()
            .newBuilder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                val originalHttpUrl = chain.request().url
                val url = originalHttpUrl
                    .newBuilder()
                    .addQueryParameter("api_key", BuildConfig.NasaAPIKey)
                    .build()
                request.url(url)
                chain.proceed(request.build())
            }
            .addInterceptor(HttpLoggingInterceptor { message ->
                Timber.tag(
                    "OkHttp"
                ).i(message)
            }.apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
        )
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val nasa = retrofit.create(NASAService::class.java)
}
