package com.jm.appsolve_fe.network

data class SignUpRequest(
    val email: String,
    val loginId: String,
    val password: String,
    val constitution: Int = 0,
    val locationPostRequestDto: LocationPostRequestDto,
    val closetUpdateRequestDto: ClosetUpdateRequestDto,
    val tasteIds: List<Int> = emptyList()
)

data class LocationPostRequestDto(
    val locationInfo: String,
    val locationIndex: Int,
    val latitude: Int,
    val longitude: Int
)

data class ClosetUpdateRequestDto(
    val uppers: List<Int> = emptyList(),
    val lowers: List<Int> = emptyList(),
    val others: List<Int> = emptyList()
)
