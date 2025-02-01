package com.jm.appsolve_fe.network

import android.content.Context
import android.util.Log
import com.jm.appsolve_fe.SignUpDataStore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

object TokenManager {
    fun refreshAccessToken(context: Context, callback: (Boolean) -> Unit) {
        val refreshToken = SignUpDataStore.getData(context, "refreshToken") ?: ""
        val deviceId = UUID.randomUUID().toString()  // 동적 deviceId 생성

        val requestBody = RefreshTokenRequest(refreshToken, deviceId)

        // 🔹 RetrofitClient.create(context) 사용
        val authService = RetrofitClient.signUpService.refreshAccessToken(requestBody)

        authService.enqueue(object : Callback<RefreshTokenResponse> {
            override fun onResponse(call: Call<RefreshTokenResponse>, response: Response<RefreshTokenResponse>) {
                if (response.isSuccessful) {
                    response.body()?.result?.let { result ->
                        SignUpDataStore.saveData(context, "accessToken", result.accessToken)
                        Log.d("TokenManager", "새로운 AccessToken 저장 완료")
                        callback(true)  // 성공 시 true 반환
                    } ?: run {
                        Log.e("TokenManager", "토큰 갱신 실패: 응답이 null")
                        callback(false)
                    }
                } else {
                    Log.e("TokenManager", "토큰 갱신 실패: ${response.code()}")
                    callback(false)
                }
            }

            override fun onFailure(call: Call<RefreshTokenResponse>, t: Throwable) {
                Log.e("TokenManager", "토큰 갱신 네트워크 오류: ${t.message}")
                callback(false)
            }
        })
    }
}