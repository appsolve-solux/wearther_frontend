package com.jm.appsolve_fe

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.jm.appsolve_fe.retrofit.LWRetrofitClient
import com.jm.appsolve_fe.retrofit.homeRecommendResponse
import com.jm.appsolve_fe.retrofit.homeRecommendResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClothesRecommendToHome(private val context: Context) {

    // 이미지에 맞는 텍스트를 반환하는 함수
    private fun getClothingText(category: String, imageIndex: Int): String {
        return when (category) {
            "upper" -> when (imageIndex) {
                1 -> "민소매"
                2 -> "반소매"
                3 -> "맨투맨 / 후드티"
                4 -> "셔츠 / 블라우스"
                5 -> "니트"
                6 -> "오프숄더"
                7 -> "히트택"
                8 -> "기모제품"
                9 -> "가디건"
                10 -> "사파리 자켓"
                11 -> "트위드 자켓"
                12 -> "레더 자켓"
                13 -> "트렌치 코트"
                14 -> "숏코트"
                15 -> "무스탕"
                16 -> "경량 패딩"
                17 -> "롱패딩"
                18 -> "숏패딩"
                19 -> "점퍼"
                20 -> "후드집업"
                21 -> "바람막이"
                else -> "상의"
            }
            "lower" -> when (imageIndex) {
                1 -> "숏 팬츠"
                2 -> "슬랙스"
                3 -> "데님"
                4 -> "와이드 팬츠"
                5 -> "스키니"
                6 -> "부츠컷"
                7 -> "조거 팬츠"
                8 -> "점프 수트"
                9 -> "반바지"
                10 -> "기모바지"
                11 -> "미니 스커트"
                12 -> "미디 스커트"
                13 -> "롱스커트"
                else -> "하의"
            }
            "other" -> when (imageIndex) {
                1 -> "쪼리"
                2 -> "목도리 & 장갑"
                3 -> "어그 & 귀마개"
                4 -> "우산"
                5 -> "장화"
                6 -> "양산"
                7 -> "비니 & 벙거지"
                8 -> "볼캡"
                9 -> "선글라스"
                else -> "기타"
            }
            else -> "Unknown Category"
        }
    }
    // 이미지 리소스 매핑 (upper, lower, other)
    private fun getClothingImageResource(category: String, imageIndex: Int): Int {
        return when (category) {
            "upper" -> when (imageIndex) {
                1 -> R.drawable.clothes_upper_1
                2 -> R.drawable.clothes_upper_2
                3 -> R.drawable.clothes_upper_3
                4 -> R.drawable.clothes_upper_4
                5 -> R.drawable.clothes_upper_5
                6 -> R.drawable.clothes_upper_6
                7 -> R.drawable.clothes_upper_7
                8 -> R.drawable.clothes_upper_8
                9 -> R.drawable.clothes_upper_9
                10 -> R.drawable.clothes_upper_10
                11 -> R.drawable.clothes_upper_11
                12 -> R.drawable.clothes_upper_12
                13 -> R.drawable.clothes_upper_13
                14 -> R.drawable.clothes_upper_14
                15 -> R.drawable.clothes_upper_15
                16 -> R.drawable.clothes_upper_16
                17 -> R.drawable.clothes_upper_17
                18 -> R.drawable.clothes_upper_18
                19 -> R.drawable.clothes_upper_19
                20 -> R.drawable.clothes_upper_20
                21 -> R.drawable.clothes_upper_21
                else -> R.drawable.home_logo // 기본 이미지
            }
            "lower" -> when (imageIndex) {
                1 -> R.drawable.clothes_lower_1
                2 -> R.drawable.clothes_lower_2
                3 -> R.drawable.clothes_lower_3
                4 -> R.drawable.clothes_lower_4
                5 -> R.drawable.clothes_lower_5
                6 -> R.drawable.clothes_lower_6
                7 -> R.drawable.clothes_lower_7
                8 -> R.drawable.clothes_lower_8
                9 -> R.drawable.clothes_lower_9
                10 -> R.drawable.clothes_lower_10
                11 -> R.drawable.clothes_lower_11
                12 -> R.drawable.clothes_lower_12
                13 -> R.drawable.clothes_lower_13
                else -> R.drawable.home_logo // 기본 이미지
            }
            "others" -> when (imageIndex) {
                1 -> R.drawable.clothes_others_1
                2 -> R.drawable.clothes_others_2
                3 -> R.drawable.clothes_others_3
                4 -> R.drawable.clothes_others_4
                5 -> R.drawable.clothes_others_5
                6 -> R.drawable.clothes_others_6
                7 -> R.drawable.clothes_others_7
                8 -> R.drawable.clothes_others_8
                9 -> R.drawable.clothes_others_9
                else -> R.drawable.home_logo // 기본 이미지
            }
            else -> R.drawable.home_logo // 기본 이미지
        }
    }

    private fun updateClothingImagesTexts(result: homeRecommendResult) {
        val activity = context as Activity

        val imgUpper: ImageView = activity.findViewById(R.id.recommendUpperImg) ?: return
        val imgLower: ImageView = activity.findViewById(R.id.recommendLowerImg) ?: return
        val imgOther: ImageView = activity.findViewById(R.id.recommendOthersImg) ?: return

        val textUpper: TextView = activity.findViewById(R.id.recommendUpperText) ?: return
        val textLower: TextView = activity.findViewById(R.id.recommendLowerText) ?: return
        val textOther: TextView = activity.findViewById(R.id.recommendOthersText) ?: return

        val recommendText1: TextView = activity.findViewById(R.id.tv_1stclothes) ?: return
        val recommendText2: TextView = activity.findViewById(R.id.tv_2ndclothes) ?: return

        // 카테고리에 따른 이미지 리소스 매핑
        val upperImageRes = getClothingImageResource("upper", result.upper.firstOrNull() ?: 0)
        val lowerImageRes = getClothingImageResource("lower", result.lower.firstOrNull() ?: 0)
        val otherImageRes = getClothingImageResource("other", result.other.firstOrNull() ?: 0)

        // 각 ImageView에 이미지 설정
        imgUpper.setImageResource(upperImageRes)
        imgLower.setImageResource(lowerImageRes)
        imgOther.setImageResource(otherImageRes)

        // 추천 옷 텍스트 설정
        textUpper.text = getClothingText("upper", result.upper.firstOrNull() ?: 0)
        textLower.text = getClothingText("lower", result.lower.firstOrNull() ?: 0)
        textOther.text = getClothingText("other", result.other.firstOrNull() ?: 0)

        recommendText1.text = getClothingText("upper",result.upper.firstOrNull() ?: 0)
        recommendText2.text = getClothingText("lower",result.lower.firstOrNull() ?: 0)

        // 날씨 정보 텍스트
        val personalWeatherTextView: TextView = activity.findViewById(R.id.tv_personalWeather) ?: return
        personalWeatherTextView.text = result.weatherInfo // 날씨 정보 표시
    }

    fun getRecommendedClothes(locationIndex: Int, latitude: Double, longitude: Double) {
        val token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxNCIsImlhdCI6MTczODMxMDM4OCwiZXhwIjoxNzQwOTAyMzg4fQ.Pma0mIzOiSPvUto6Ya8qs1Cncoz5W2QBkOiZiGkiD40"

        LWRetrofitClient.service.homeRecommend("Bearer $token", locationIndex, latitude, longitude)
            .enqueue(object : Callback<homeRecommendResponse> {
                override fun onResponse(
                    call: Call<homeRecommendResponse>,
                    response: Response<homeRecommendResponse>
                ) {
                    if (response.isSuccessful && response.body()?.success == true) {
                        val recommendResult = response.body()?.result
                        recommendResult?.let {
                            updateClothingImagesTexts(it)
                        }
                    } else {
                        Log.e("WeatherAPI", "Failed to get recommendations: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<homeRecommendResponse>, t: Throwable) {
                    Log.e("WeatherAPI", "Error: ${t.message}")
                }
            })
    }
}