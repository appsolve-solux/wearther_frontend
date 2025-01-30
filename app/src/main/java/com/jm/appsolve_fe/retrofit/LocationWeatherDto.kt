package com.jm.appsolve_fe.retrofit

data class PostBookmarkLocationRequest(
    val locationInfo: String,
    val locationIndex: Int,
    val latitude: Double,
    val longitude: Double
)

data class PostBookmarkLocationResponse(
    val httpStatus: String,
    val success: Boolean,
    val result: PostBookmarkLocationResult?,
    val error: String?
)

data class PostBookmarkLocationResult(
    val locationInfo: String,
    val locationIndex: Int,
    val latitude: Double,
    val longitude: Double
)

data class GetBookmarkLocationResponse(
    val httpStatus: String,
    val success: Boolean,
    val result: GetBookmarkLocationResult?,
    val error: Any?
)

data class GetBookmarkLocationResult(
    val locations: List<GetBookmarkLocations>
)

data class GetBookmarkLocations(
    val locationInfo: String,
    val locationIndex: Int,
    val temperature: String
)

data class DeleteBookmarkLocationRequest(
    val httpStatus: String,
    val success: Boolean,
    val result: Int,
    val error: String?
)

data class updateBookmarkLocationIndexRequest(
    val beforeLocationIndex: Int,
    val afterLocationIndex: Int
)

data class updateBookmarkLocationIndexResponse(
    val httpStatus: String,
    val success: Boolean,
    val result: updateBookmarkLocationIndexRequest?,
    val error: String?
)

data class currentBookmarkLocationWeatherResponse(
    val httpStatus: String,
    val success: Boolean,
    val result: String,
    val error: String?
)

data class homeCurrentLocationWeatherResponse(
    val httpStatus: String,
    val success: Boolean,
    val result: homeCurrentLocationWeatherResult?,
    val error: String?
)

data class homeCurrentLocationWeatherResult(
    val temperature: String,
    val temperatureMin: String,
    val temperatureMax: String,
    val humidity: String,
    val hourlyTemp: List<String>,
    val hourlySky: List<String>,
    val rain: String
)

data class homeRecommendResponse(
    val httpStatus: String,
    val success: Boolean,
    val result: homeRecommendResult?,
    val error: String?
)

data class homeRecommendResult(
    val upper: List<Int>,
    val lower: List<Int>,
    val other: List<Int>
)