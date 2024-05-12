package com.veo.googlemapdemo.model

import retrofit2.http.GET
import retrofit2.http.Query

interface IGoogleMapService {

    @GET("/maps/api/directions/json?")
    suspend fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") key: String,
        @Query("mode") mode: String = "driving"
    ): DirectionsResponse

}