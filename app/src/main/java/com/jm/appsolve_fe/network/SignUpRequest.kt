package com.jm.appsolve_fe.network

data class SignUpRequest(
    val email: String,
    val loginId: String,
    val password: String,
    val constitution: Int,
    val locationPostRequestDto: LocationPostRequestDto,
    val closetUpdateRequestDto: ClosetUpdateRequestDto,
    val tasteIds: List<Int>
)

data class LocationPostRequestDto(
    val locationInfo: String,
    val locationIndex: Int,
    val latitude: Int,
    val longitude: Int
)

data class ClosetUpdateRequestDto(
    val uppers: List<Int>,
    val lowers: List<Int>,
    val others: List<Int>
)
