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
