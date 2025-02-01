package com.jm.appsolve_fe

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.jm.appsolve_fe.retrofit.GetBookmarkLocationResponse
import com.jm.appsolve_fe.retrofit.GetBookmarkLocations
import com.jm.appsolve_fe.retrofit.LWRetrofitClient
import com.jm.appsolve_fe.retrofit.homeCurrentLocationWeatherResponse
import com.jm.appsolve_fe.retrofit.homeCurrentLocationWeatherResult
import com.jm.appsolve_fe.retrofit.homeRecommendResponse
import com.jm.appsolve_fe.retrofit.homeRecommendResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Home : Fragment() {

    private lateinit var dateText : TextView

    private lateinit var hwrecyclerView: RecyclerView
    private var wList = ArrayList<HomeWeatherData>()
    private lateinit var hwadapter: HomeWeatherAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    private val now: LocalDate = LocalDate.now()

    //버튼
    private lateinit var locationBtn1: Button
    private lateinit var locationBtn2: Button
    private lateinit var locationBtn3: Button

    //현재 위치 위경도
    private lateinit var currentLocation: GetCurrentLocation
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var tvTemperature: TextView
    private lateinit var tvHumidity: TextView
    private lateinit var tvMinTemperature: TextView
    private lateinit var tvMaxTemperature: TextView

    // Fragment에서 받을 매개변수
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxNCIsImlhdCI6MTczODMxMDM4OCwiZXhwIjoxNzQwOTAyMzg4fQ.Pma0mIzOiSPvUto6Ya8qs1Cncoz5W2QBkOiZiGkiD40"

        // 위치 버튼 동적 생성
        getBookmarkLocation(token) { locations ->
            if (locations != null) {
                // 위치 버튼들을 동적으로 생성
                createLocationButtons(view, locations)
            }
        }

        // 오늘 날짜 출력
        dateText = view.findViewById(R.id.dateText)
        val formattedDate = now.format(DateTimeFormatter.ofPattern("MM월 dd일"))
        dateText.text = formattedDate


        // 버튼 클릭 시 LocationActivity로 이동
        val gotoLocationButton: ImageButton = view.findViewById(R.id.gotoLocationBtn)
        gotoLocationButton.setOnClickListener {
            // LocationActivity로 이동
            (activity as HomeMainActivity).replaceFragment(Location())
        }

        // GetCurrentLocation 객체 생성
        currentLocation = GetCurrentLocation(requireContext())
        tvTemperature = view.findViewById(R.id.tv_temperature)
        tvMinTemperature = view.findViewById(R.id.tv_mintemperature)
        tvMaxTemperature = view.findViewById(R.id.tv_maxtemperature)
        tvHumidity = view.findViewById(R.id.tv_humidity)

        // 위치 버튼 설정
        locationBtn1 = view.findViewById(R.id.locationBtn1)
        locationBtn2 = view.findViewById(R.id.locationBtn2)
        locationBtn3 = view.findViewById(R.id.locationBtn3)
        locationBtn1.isSelected = true


        locationBtn1.setOnClickListener { updateLocationSelection(locationBtn1, 1) }
        locationBtn2.setOnClickListener { updateLocationSelection(locationBtn2, 2) }
        locationBtn3.setOnClickListener { updateLocationSelection(locationBtn3, 3) }

        // 현재 위치 날씨 자동 로드
        getCurrentLocationAndLoadWeather()

        val clothesItemLayout: LinearLayout = view.findViewById(R.id.recommendClothes1)

        clothesItemLayout.setOnClickListener {
            // 다른 Fragment로 이동
            (activity as HomeMainActivity).replaceFragment(TodayRecommended())
        }
        
        // 시간별 날씨 리사이클러뷰
        hwrecyclerView = view.findViewById(R.id.home_weather_recyclerView)
        hwrecyclerView.setHasFixedSize(true)
        hwrecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        hwadapter = HomeWeatherAdapter(wList)
        hwrecyclerView.adapter = hwadapter

        return view
    }
    
    // 텍스트용 api 호출
    fun getBookmarkLocation(token: String, callback: (List<GetBookmarkLocations>?) -> Unit) {
        LWRetrofitClient.service.getBookmarkLocation("Bearer $token")
            .enqueue(object : Callback<GetBookmarkLocationResponse> {
                override fun onResponse(
                    call: Call<GetBookmarkLocationResponse>,
                    response: Response<GetBookmarkLocationResponse>
                ) {
                    if (response.isSuccessful && response.body()?.success == true) {
                        // 버튼에 사용할 위치 데이터 목록 반환
                        callback(response.body()?.result?.locations)
                    } else {
                        Log.e("WeatherAPI", "Failed to get locations: ${response.errorBody()?.string()}")
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<GetBookmarkLocationResponse>, t: Throwable) {
                    Log.e("WeatherAPI", "Error: ${t.message}")
                    callback(null)
                }
            })
    }
    private fun createLocationButtons(view: View, locations: List<GetBookmarkLocations>) {

        Log.d("button","불러")
        val buttonContainer: LinearLayout = view.findViewById(R.id.buttonlinearlayout)
        val tvAdminareaH: TextView = view.findViewById(R.id.tv_adminareaH)
        val tvlocalityH: TextView = view.findViewById(R.id.tv_localityH)
        val tvlocationText: TextView = view.findViewById(R.id.locationText)

        buttonContainer.removeAllViews()

        var firstButton: Button? = null

        locations.forEachIndexed { index, location ->
            val locationWords = location.locationInfo.split(" ")
            val secondWord = locationWords.getOrNull(1) ?: ""
            val thirdWord = locationWords.getOrNull(2) ?: ""
            val fourthWord = locationWords.getOrNull(3)?:""
            val button = Button(requireContext()).apply {
                val lastWord = locationWords.lastOrNull() ?: location.locationInfo
                text = lastWord

                setOnClickListener {
                    tvAdminareaH.text = secondWord
                    tvlocalityH.text = thirdWord

                    tvlocationText.text = "$thirdWord $fourthWord"
                    getHomeWeatherData(location.locationIndex)
                }
            }
            if (index == 0) {
                firstButton = button // 첫 번째 버튼 저장
            }
            buttonContainer.addView(button)
        }
        // 첫 번째 버튼 자동 선택
        firstButton?.let {
            it.isSelected = true
            it.performClick()
        }
    }

    private fun updateLocationSelection(selectedButton: Button, locationIndex: Int) {
        val locationBtns = listOf(
            requireView().findViewById<Button>(R.id.locationBtn1),
            requireView().findViewById<Button>(R.id.locationBtn2),
            requireView().findViewById<Button>(R.id.locationBtn3)
        )

        locationBtns.forEach { it.isSelected = it == selectedButton }

        // locationIndex 값이 null 또는 0이 아니면 날씨 데이터 요청
        if (locationIndex != 0) {
            Log.d("WeatherAPI", "00Invalid locationIndex: $locationIndex")
            getHomeWeatherData(locationIndex)
        } else {
            getHomeWeatherData(0)
            Log.e("WeatherAPI", "Invalid locationIndex: $locationIndex")
        }
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

        val imgUpper: ImageView = view?.findViewById(R.id.recommendUpperImg) ?: return
        val imgLower: ImageView = view?.findViewById(R.id.recommendLowerImg) ?: return
        val imgOther: ImageView = view?.findViewById(R.id.recommendOthersImg) ?: return

        val textUpper: TextView = view?.findViewById(R.id.recommendUpperText) ?: return
        val textLower: TextView = view?.findViewById(R.id.recommendLowerText) ?: return
        val textOther: TextView = view?.findViewById(R.id.recommendOthersText) ?: return

        val recommendText1: TextView = view?.findViewById(R.id.tv_1stclothes) ?: return
        val recommendText2: TextView = view?.findViewById(R.id.tv_2ndclothes) ?: return

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
        val personalWeatherTextView: TextView = view?.findViewById(R.id.tv_personalWeather) ?: return
        personalWeatherTextView.text = result.weatherInfo // 날씨 정보 표시
    }
    
    //예보 api
    private fun getHomeWeatherData(locationIndex: Int) {
        val token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxNCIsImlhdCI6MTczODMxMDM4OCwiZXhwIjoxNzQwOTAyMzg4fQ.Pma0mIzOiSPvUto6Ya8qs1Cncoz5W2QBkOiZiGkiD40" // SharedPreferences에서 가져오거나 변경

        // locationIndex가 0이면 현재 위치, 1,2,3은 각 버튼에 해당하는 위치
        if (locationIndex == 0) {
            // 현재 위치를 기준 날씨
            if (latitude != 0.0 && longitude != 0.0) {  // 위경도가 유효한지 확인
                Log.d("WeatherAPI??", "현재 위치의 위도: $latitude, 경도: $longitude")
                LWRetrofitClient.service.homeCurrentLocationWeather(
                    "Bearer $token", locationIndex, latitude, longitude
                ).enqueue(object : Callback<homeCurrentLocationWeatherResponse> {
                    override fun onResponse(
                        call: Call<homeCurrentLocationWeatherResponse>,
                        response: Response<homeCurrentLocationWeatherResponse>
                    ) {
                        if (response.isSuccessful && response.body()?.success == true) {
                            response.body()?.result?.let {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    updateWeatherUI(it)
                                } else {
                                    Log.e("WeatherAPI", "This feature is not supported on API level < 26")
                                }
                            }
                        } else {
                            Log.d("WeatherAPI123", "현재 위치의 위도: $latitude, 경도: $longitude")
                            Log.e("WeatherAPI11", "Failed: ${response.errorBody()?.string()}")
                        }
                    }

                    override fun onFailure(call: Call<homeCurrentLocationWeatherResponse>, t: Throwable) {
                        Log.e("WeatherAPI", "Error: ${t.message}")
                    }
                })
            } else {
                Log.e("WeatherAPI", "위도 또는 경도 값이 유효하지 않습니다.")
            }
        } else {
            // 선택된 위치의 날씨
            LWRetrofitClient.service.homeCurrentLocationWeather(
                "Bearer $token", locationIndex, latitude, longitude
            ).enqueue(object : Callback<homeCurrentLocationWeatherResponse> {
                override fun onResponse(
                    call: Call<homeCurrentLocationWeatherResponse>,
                    response: Response<homeCurrentLocationWeatherResponse>
                ) {
                    if (response.isSuccessful && response.body()?.success == true) {
                        response.body()?.result?.let {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                updateWeatherUI(it)
                                getRecommendedClothes(locationIndex,latitude,longitude)
                            } else {
                                Log.e("WeatherAPI", "This feature is not supported on API level < 26")
                            }
                        }
                    } else {
                        Log.e("WeatherAPI", "Failed: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<homeCurrentLocationWeatherResponse>, t: Throwable) {
                    Log.e("WeatherAPI", "Error: ${t.message}")
                }
            })
        }
    }

    private fun getCurrentLocationAndLoadWeather() {
        Log.d("HomeFragment", "getCurrentLocationAndLoadWeather 호출됨")
        currentLocation.getCurrentLocation { lat, lon ->
            // 디버깅용 로그
            Log.d("Location111", "위도: $latitude, 경도: $longitude")
            latitude = lat
            longitude = lon
            Log.d("Location222", "위도: $latitude, 경도: $longitude")

            // 날씨 데이터 가져오는 함수 호출 -> 기본은 1
            getHomeWeatherData(1) 
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateWeatherUI(result: homeCurrentLocationWeatherResult) {

        val currentTemp = result.temperature.replace("°C", "°")
        val currentMinTemp = result.temperatureMin.replace("°C", "°")
        val currentMaxTemp = result.temperatureMax.replace("°C", "°")
        val currentHum = result.humidity.replace("%", "")
        tvTemperature.text = currentTemp
        tvMinTemperature.text = currentMinTemp
        tvMaxTemperature.text = currentMaxTemp
        tvHumidity.text = "$currentHum%"

        // 습도 조건에 따른 텍스트 설정
        val humidityValue = currentHum.toIntOrNull() ?: 0 // humidity가 숫자가 아닐 경우 0으로 처리

        val humidityCondition = when {
            humidityValue in 0..20 -> "매우 낮음"
            humidityValue in 21..40 -> "낮음"
            humidityValue in 41..60 -> "보통"
            humidityValue in 61..80 -> "높음"
            humidityValue >= 81 -> "매우 높음"
            else -> "정보 없음"
        }

        // 습도 상태 텍스트 설정
        val humidityConditionTextView: TextView = view?.findViewById(R.id.tv_humidcondition) ?: return
        humidityConditionTextView.text = humidityCondition

        val now = LocalDateTime.now()

        val hourlyTemp = result.hourlyTemp.map { it.replace("°C", "°") }
        val hourlySky = result.hourlySky

        wList.clear() // 기존 데이터 초기화

        // 시간별 데이터 추가
        for (i in 0 until hourlyTemp.size) {
            val currentHour = now.plusHours(i.toLong())
            val hourFormatted = currentHour.format(DateTimeFormatter.ofPattern("HH시"))
            val temperature = hourlyTemp.getOrElse(i) { "0°" } // 기본값 설정
            val sky = hourlySky.getOrElse(i) { "흐림" } // 기본값 설정

            // 날씨 이미지 설정
            val weatherImage = when (sky) {
                "맑음" -> R.drawable.weather_sun
                "구름 조금" -> R.drawable.weather_sun_cloudy
                "구름 많음" -> R.drawable.weather_cloudy_sun
                else -> R.drawable.weather_cloudy
            }

            wList.add(HomeWeatherData(hourFormatted, weatherImage, temperature))
        }
        hwadapter.notifyDataSetChanged()
    }
}
