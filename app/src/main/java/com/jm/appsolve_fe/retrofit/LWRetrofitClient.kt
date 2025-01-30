package com.jm.appsolve_fe.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object LWRetrofitClient {

    private const val BASE_URL = "http://54.180.114.64:8080/"

    // OkHttpClient
    private val client: OkHttpClient = OkHttpClient.Builder().apply {
        // 로깅 추가 HTTP 요청 응답 확인
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        addInterceptor(loggingInterceptor)
    }.build()

    // Retrofit 객체
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: LocationWeatherService = retrofit.create(LocationWeatherService::class.java)
}