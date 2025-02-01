package com.jm.appsolve_fe.retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface LocationWeatherService {

    // 즐겨찾기
    @POST("/location/post") //+
    fun postBookmarkLocation(
        @Header("Authorization") token: String,
        @Body request: PostBookmarkLocationRequest
    ): Call<PostBookmarkLocationResponse>

    @GET("/location/get") //+
    fun getBookmarkLocation(
        @Header("Authorization") token: String
    ): Call<GetBookmarkLocationResponse>

    @DELETE("/location/delete/{locationindex}") //+
    fun deleteBookmarkLocation(
        @Header("Authorization") token: String,
        @Path("locationindex") locationIndex: Int
    ): Call<DeleteBookmarkLocationRequest>

    @PATCH("/location/update-index")
    fun updateBookmarkLocationIndex(
        @Header("Authorization") token: String,
        @Body request: updateBookmarkLocationIndexRequest
    ): Call<updateBookmarkLocationIndexResponse>

    //즐겨찾기 현재 위치 날씨
    @GET("/location/current-tmp/{latitude}/{longitude}") //+
    fun currentBookmarkLocationWeather(
        @Header("Authorization") token: String,
        @Path("latitude") latitude: Double,
        @Path("longitude") longitude: Double
    ): Call<currentBookmarkLocationWeatherResponse>

    //홈 현재 위치 날씨
    @GET("/home/weather/{locationIndex}")
    fun homeCurrentLocationWeather(
        @Header("Authorization") token: String,
        @Path("locationIndex") locationIndex: Int,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double

    ): Call<homeCurrentLocationWeatherResponse>

    //오늘의 추천템 조회
    @GET("/home/recommend/{locationIndex}")
    fun homeRecommend(
        @Header("Authorization") token: String,
        @Path("locationIndex") locationIndex: Int,
        latitude: Double,
        longitude: Double
    ):Call<homeRecommendResponse>

}