package com.example.wanderlog.api.service

import com.example.wanderlog.database.dto.LoginRequest
import com.example.wanderlog.database.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Optional

interface UserService {
    @GET("/api/v1/user/{id}")
    fun getUserById(@Path("id") id: String): Call<Optional<User>>

    @GET("/api/v1/user/email")
    fun getUserByEmail(@Query("email") email: String): Call<Optional<User>>

    @POST("/api/v1/user/save")
    fun saveUser(@Body user: User): Call<Optional<User>>

    @PUT("/api/v1/user/update/{id}")
    fun updateUserById(@Path("id") id: String, @Body user: User): Call<Optional<User>>

    @POST("/api/v1/auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<Boolean>
}