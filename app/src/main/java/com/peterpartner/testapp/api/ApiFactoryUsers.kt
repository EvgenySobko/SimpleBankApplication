package com.peterpartner.testapp.api

import com.peterpartner.testapp.entities.UserList
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

object ApiFactoryUsers {

    val service = getBuilder().create(ApiServiceUsersImpl::class.java)

    fun buildClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(90, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(90, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    private fun getBuilder(): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://hr.peterpartner.net/test/android/v1/")
            .client(buildClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}

interface ApiServiceUsersImpl {

    @GET("users.json")
    suspend fun getHolders(): UserList
}