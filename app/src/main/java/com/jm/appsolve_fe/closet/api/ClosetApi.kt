package com.jm.appsolve_fe.closet.api


import com.jm.appsolve_fe.closet.data.ClosestResponseWrapper
import com.jm.appsolve_fe.closet.data.ShoppingResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface ClosetApi {
    @GET("closet/recommend")
    fun getRecommendedCloset(): Call<ClosestResponseWrapper>

    @GET("closet/clothes")
    fun getClosetData(
        @Header("Authorization") token: String
    ): Call<ClosestResponseWrapper>

    @GET("closet/shopping")
    fun getShoppingRecommendations(): Call<ShoppingResponse>
}