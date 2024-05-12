package com.veo.googlemapdemo.network

import com.veo.googlemapdemo.model.IGoogleMapService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIService private constructor(){

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getGoogleMapService(): IGoogleMapService = retrofit.create(IGoogleMapService::class.java)

    companion object {

        val instance: APIService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            APIService()
        }
    }
}