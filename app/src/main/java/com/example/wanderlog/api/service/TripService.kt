package com.example.wanderlog.api.service

import com.example.wanderlog.database.models.Trip
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Optional

interface TripService {
    @POST("/api/v1/trips/create")
    fun createTrip(@Body trip: Trip): Call<Trip>

    @GET("/api/v1/trips/trip/{id}")
    fun getTripById(@Path("id") id: String): Call<Trip>

    @GET("/api/v1/trips/user")
    fun getTripsByUserId(@Query("userId") userId: String): Call<List<Trip>>

    @PUT("/api/v1/trips/update/{id}")
    fun updateTripById(@Path("id") id: String, @Body trip: Trip): Call<Optional<Optional<Trip>>>
}