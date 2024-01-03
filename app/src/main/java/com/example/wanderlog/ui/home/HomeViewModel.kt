package com.example.wanderlog.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wanderlog.api.service.TripService
import com.example.wanderlog.database.models.Trip
import com.example.wanderlog.retrofit.RetrofitInstance
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _trips = MutableLiveData<Set<Trip>>()
    val trips: LiveData<Set<Trip>> = _trips

    fun fetchTrips(userId: String) {
        val tripService: TripService = RetrofitInstance.getRetrofitInstance().create(TripService::class.java)
        tripService.getTripsByUserId(userId).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    response.body()?.string()?.let {
                        val gson = Gson()
                        val tripListType = object : TypeToken<Set<Trip>>() {}.type
                        val trips = gson.fromJson<Set<Trip>>(it, tripListType)
                        _trips.postValue(trips)
                    }
                } else {
                    Log.e("HomeViewModel", "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("HomeViewModel", "Failure: ${t.message}")
            }
        })
    }

}