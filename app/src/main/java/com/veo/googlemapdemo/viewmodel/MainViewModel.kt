package com.veo.googlemapdemo.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.veo.googlemapdemo.model.DirectionsResponse
import com.veo.googlemapdemo.model.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel : ViewModel() {

    val directions = MutableLiveData<DirectionsResponse>()

    var originLat: Double = 0.0
    var originLng: Double = 0.0

    val testLat = 22.33
    val testLng = 113.49

    fun getDirections(
        dstLat: Double, dstLng: Double
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "Start = $originLat,$originLng End = $dstLat,$dstLng")
            try {
                val result = MainRepository.getMapDirections(
                    "$originLat,$originLng",
                    "$dstLat,$dstLng"
                )
                directions.postValue(result)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    companion object{
        const val TAG = "MainViewModel"
    }
}