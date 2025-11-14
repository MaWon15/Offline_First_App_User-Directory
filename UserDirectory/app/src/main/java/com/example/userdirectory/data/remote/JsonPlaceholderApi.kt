package com.example.userdirectory.data.remote

import retrofit2.http.GET

interface JsonPlaceholderApi {
    @GET("users")
    suspend fun getUsers(): List<UserDto>
}
