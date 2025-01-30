package com.jm.appsolve_fe.network

data class SignUpResponse(
    val httpStatus: String,
    val success: Boolean,
    val result: Result?,
    val error: String?
) {
    data class Result(
        val memberId: Int? = null,
        val isDuplicated: Boolean? = null
    )
}

data class LoginResponse(
    val httpStatus: String,
    val success: Boolean,
    val result: Result?,
    val error: String?
) {
    data class Result(
        val accessToken: String,
        val refreshToken: String,
        val memberId: Int
    )
}

data class RefreshTokenResponse(
    val httpStatus: String,
    val success: Boolean,
    val result: Result?,
    val error: ErrorResponse?
) {
    data class Result(
        val accessToken: String
    )

    data class ErrorResponse(
        val code: String,
        val message: String
    )
}