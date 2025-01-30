package com.jm.appsolve_fe.network

import android.content.Context
import android.content.Intent
import com.jm.appsolve_fe.MainActivity
import com.jm.appsolve_fe.SignUpDataStore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object SignUpManager {
    fun sendSignUpRequest(context: Context) {
        val loginId = SignUpDataStore.getData(context, "loginId") ?: ""
        val password = SignUpDataStore.getData(context, "password") ?: ""
        val email = SignUpDataStore.getData(context, "email") ?: ""
        val constitution = SignUpDataStore.getData(context, "constitution")?.toInt() ?: 0

        val locationInfo = SignUpDataStore.getData(context, "locationInfo") ?: ""
        val locationIndex = SignUpDataStore.getData(context, "locationIndex")?.toInt() ?: 0

        val tasteIds = SignUpDataStore.getData(context, "tasteIds")
            ?.split(",")
            ?.mapNotNull { it.toIntOrNull() }
            ?: emptyList()

        val uppers = SignUpDataStore.getData(context, "uppers")
            ?.split(",")
            ?.mapNotNull { it.toIntOrNull() }
            ?: emptyList()

        val lowers = SignUpDataStore.getData(context, "lowers")
            ?.split(",")
            ?.mapNotNull { it.toIntOrNull() }
            ?: emptyList()

        val others = SignUpDataStore.getData(context, "others")
            ?.split(",")
            ?.mapNotNull { it.toIntOrNull() }
            ?: emptyList()

        val requestBody = SignUpRequest(
            loginId = loginId,
            password = password,
            email = email,
            constitution = constitution,
            locationPostRequestDto = LocationPostRequestDto(
                locationInfo = locationInfo,
                locationIndex = locationIndex,
                latitude = 0,
                longitude = 0
            ),
            closetUpdateRequestDto = ClosetUpdateRequestDto(
                uppers = uppers,
                lowers = lowers,
                others = others
            ),
            tasteIds = tasteIds
        )

        RetrofitClient.apiService.signUp(requestBody).enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                if (response.isSuccessful) {
                    context.startActivity(Intent(context, MainActivity::class.java))
                } else {
                    // 실패 처리
                }
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                // 네트워크 오류 처리
            }
        })
    }
}
