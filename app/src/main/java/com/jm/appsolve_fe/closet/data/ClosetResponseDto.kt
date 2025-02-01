package com.jm.appsolve_fe.closet.data

data class ClosetResponseDto(
    val uppers: List<Int>,
    val lowers: List<Int>,
    val others: List<Int>
)