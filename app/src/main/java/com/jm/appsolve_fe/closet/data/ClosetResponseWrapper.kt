package com.jm.appsolve_fe.closet.data


data class ClosetResponseWrapper(
    val httpStatus: String,
    val success: Boolean,
    val result: ClosetResponseDto?,
    val error: String?
)