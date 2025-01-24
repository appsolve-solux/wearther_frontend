package com.jm.appsolve_fe.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

// 서버 API와 매핑되는 함수 정의
interface SignUpService {
    @POST("/member/signUp") // 이메일 유효성 검사 URL
    fun signUp(@Body emailRequest: SignUpRequest): Call<SignUpResponse>
}