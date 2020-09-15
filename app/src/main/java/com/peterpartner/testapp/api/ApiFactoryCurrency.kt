package com.peterpartner.testapp.api

import com.peterpartner.testapp.entities.Currency
import com.peterpartner.testapp.entities.UserList
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

object ApiFactoryCurrency {

    val service = getBuilder().create(ApiServiceCurrencyImpl::class.java)

    fun buildClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(90, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(90, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    private fun getBuilder(): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://www.cbr-xml-daily.ru/")
            .client(buildClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}

interface ApiServiceCurrencyImpl {

    @GET("daily_json.js")
    suspend fun getCurrency(): Currency
}