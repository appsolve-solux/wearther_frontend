package com.jm.appsolve_fe.closet.api


import com.jm.appsolve_fe.closet.data.ClosetResponseWrapper
import com.jm.appsolve_fe.closet.data.ShoppingResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface ClosetApi {
    @GET("closet/recommend")
    fun getRecommendedCloset(
        @Header("Authorization") token: String
    ): Call<ClosetResponseWrapper>

    @GET("closet/clothes")
    fun getClosetData(
        @Header("Authorization") token: String
    ): Call<ClosetResponseWrapper>

    @GET("closet/shopping")
    fun getShoppingData(
        @Header("Authorization") token: String
    ): Call<ShoppingResponse>
}