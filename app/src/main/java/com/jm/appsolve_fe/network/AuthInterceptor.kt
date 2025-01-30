package com.jm.appsolve_fe.network

import android.content.Context
import com.jm.appsolve_fe.SignUpDataStore
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val accessToken = SignUpDataStore.getData(context, "accessToken") ?: ""

        val requestWithToken = originalRequest.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()

        val response = chain.proceed(requestWithToken)

        if (response.code == 401) { // 토큰 만료
            TokenManager.refreshAccessToken(context) { success ->
                if (success) {
                    val newAccessToken = SignUpDataStore.getData(context, "accessToken") ?: ""
                    val newRequest = originalRequest.newBuilder()
                        .header("Authorization", "Bearer $newAccessToken")
                        .build()
                    chain.proceed(newRequest)  // 재요청
                }
            }
        }

        return response
    }
}