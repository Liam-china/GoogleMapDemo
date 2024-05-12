package com.veo.googlemapdemo.model

import com.veo.googlemapdemo.network.APIService

class MainRepository private constructor(){
    companion object {

        @JvmStatic
        suspend fun getMapDirections(
            origin: String,
            destination: String,
            key: String = "AIzaSyC34FCmNaaDf2nVGJwiLvHuxcFInoMS6ug"
        ): DirectionsResponse {
            return APIService.instance.getGoogleMapService().getDirections(
                origin, destination, key
            )
        }
    }
}