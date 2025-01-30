package com.jm.appsolve_fe.retrofit

import okhttp3.OkHttpClient
import okhttp3.Request
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

        // Authorization 헤더 추가
        addInterceptor { chain ->
            val token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwiaWF0IjoxNzM2Nzc1NjUxLCJleHAiOjE3MzkzNjc2NTF9.WMPnp2DERvHZiG2s1YqQAtB4pngjvRWv2OSAY5ldMXo" // 여기서 토큰을 불러오는 방법에 따라 수정 (예: SharedPreferences 등)
            val newRequest: Request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(newRequest)
        }

    }.build()

    // Retrofit 객체
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: LocationWeatherService = retrofit.create(LocationWeatherService::class.java)
}