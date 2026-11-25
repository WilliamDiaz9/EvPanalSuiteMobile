package com.duoc.evpanalsuitemobile.data.remote

import com.duoc.evpanalsuitemobile.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

// Microservicio de usuarios (puerto 8081)
interface UsersApiService {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): User

    @GET("api/users")
    suspend fun getUsers(): List<User>
}
