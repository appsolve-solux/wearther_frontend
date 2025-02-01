package com.jm.appsolve_fe.closet.data


data class ClosestResponseWrapper(
    val httpStatus: String,
    val success: Boolean,
    val result: ClosetResponseDto?,
    val error: String?
)