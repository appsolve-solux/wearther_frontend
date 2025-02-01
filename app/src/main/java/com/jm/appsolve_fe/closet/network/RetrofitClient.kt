package com.jm.appsolve_fe.closet.network

import com.jm.appsolve_fe.closet.api.ClosetApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://54.180.114.64:8080/"

    val instance: ClosetApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ClosetApi::class.java)
    }
}