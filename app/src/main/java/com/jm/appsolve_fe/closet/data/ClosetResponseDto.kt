package com.jm.appsolve_fe.closet.data

data class ClosetResponseDto(
    val uppers: List<Int> = emptyList(),
    val lowers: List<Int> = emptyList(),
    val others: List<Int> = emptyList()
)