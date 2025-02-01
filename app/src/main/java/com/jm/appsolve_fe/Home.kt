package com.jm.appsolve_fe

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.VectorDrawable
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
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.jm.appsolve_fe.retrofit.GetBookmarkLocationResponse
import com.jm.appsolve_fe.retrofit.GetBookmarkLocations
import com.jm.appsolve_fe.retrofit.LWRetrofitClient
import com.jm.appsolve_fe.retrofit.homeCurrentLocationWeatherResponse
import com.jm.appsolve_fe.retrofit.homeCurrentLocationWeatherResult
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
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var tvTemperature: TextView
    private lateinit var tvHumidity: TextView
    private lateinit var tvMinTemperature: TextView
    private lateinit var tvMaxTemperature: TextView

    private lateinit var clothesRecommendToHome: ClothesRecommendToHome

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
        // ClothingRecommendation 객체 초기화
        clothesRecommendToHome = ClothesRecommendToHome(requireContext())

        val clothesItemLayout1: LinearLayout = view.findViewById(R.id.recommendClothes1)
        val clothesItemLayout2: LinearLayout = view.findViewById(R.id.recommendClothes2)
        val clothesItemLayout3: LinearLayout = view.findViewById(R.id.recommendClothes3)

        clothesItemLayout1.setOnClickListener {
            val recommendUpperImg: ImageView = view.findViewById(R.id.recommendUpperImg)
            val recommendUpperText: TextView = view.findViewById(R.id.recommendUpperText)

            // recommendUpperImg의 drawable 추출
            val drawable = recommendUpperImg.drawable
            val bitmap: Bitmap?

            if (drawable is VectorDrawable) {
                // VectorDrawable을 Bitmap으로 변환
                bitmap = Bitmap.createBitmap(
                    drawable.intrinsicWidth,
                    drawable.intrinsicHeight,
                    Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
            } else if (drawable is BitmapDrawable) {
                // BitmapDrawable이면 그대로 사용
                bitmap = (drawable as BitmapDrawable).bitmap
            } else {
                // 알 수 없는 drawable 타입 처리
                bitmap = null
                Log.e("ImageTransfer", "알 수 없는 drawable 타입: $drawable")
            }

            // Bitmap을 Bundle로 전달
            bitmap?.let {
                val bundle = Bundle()
                bundle.putParcelable("imageBitmap", it)  // Bitmap 전달
                bundle.putString("recommendText", recommendUpperText.text.toString())

                // Fragment 전환
                val todayRecommendedFragment = TodayRecommended()
                todayRecommendedFragment.arguments = bundle
                (activity as HomeMainActivity).replaceFragment(todayRecommendedFragment)
            } ?: run {
                Log.e("ImageTransfer", "이미지가 없습니다.")
            }
        }
        clothesItemLayout2.setOnClickListener {
            val recommendLowerImg: ImageView = view.findViewById(R.id.recommendLowerImg)
            val recommendLowerText: TextView = view.findViewById(R.id.recommendLowerText)

            // recommendLowerImg의 drawable 추출
            val drawable = recommendLowerImg.drawable
            val bitmap: Bitmap?

            if (drawable is VectorDrawable) {
                bitmap = Bitmap.createBitmap(
                    drawable.intrinsicWidth,
                    drawable.intrinsicHeight,
                    Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
            } else if (drawable is BitmapDrawable) {
                bitmap = (drawable as BitmapDrawable).bitmap
            } else {
                bitmap = null
                Log.e("ImageTransfer", "알 수 없는 drawable 타입: $drawable")
            }

            // Bitmap을 Bundle로 전달
            bitmap?.let {
                val bundle = Bundle().apply {
                    putParcelable("imageBitmap", it)  // Bitmap 전달
                    putString("recommendText", recommendLowerText.text.toString())
                }

                // Fragment 전환
                val todayRecommendedFragment = TodayRecommended()
                todayRecommendedFragment.arguments = bundle
                (activity as HomeMainActivity).replaceFragment(todayRecommendedFragment)
            } ?: run {
                Log.e("ImageTransfer", "이미지가 없습니다.")
            }
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
            val fourthWord = locationWords.getOrNull(3) ?: ""
            val button = Button(requireContext()).apply {
                val lastWord = locationWords.lastOrNull() ?: location.locationInfo
                text = lastWord

                // 내부 padding 제거
                setPadding(0,0, 0,0)

                // 기본 스타일 설정
                val buttonBackground = GradientDrawable()
                buttonBackground.shape = GradientDrawable.RECTANGLE
                buttonBackground.cornerRadius = 70f
                buttonBackground.setColor(ContextCompat.getColor(requireContext(), R.color.white))
                buttonBackground.setStroke(3, ContextCompat.getColor(requireContext(), R.color.gray_A6A6A6))
                background = buttonBackground

                setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_A6A6A6))

                // 클릭 시 스타일 변경
                setOnClickListener {
                    // 선택된 버튼의 스타일 적용
                    buttonContainer.children.forEach {
                        val btn = it as Button
                        val defaultButtonBackground = GradientDrawable()
                        defaultButtonBackground.shape = GradientDrawable.RECTANGLE
                        defaultButtonBackground.cornerRadius = 70f
                        defaultButtonBackground.setColor(ContextCompat.getColor(requireContext(), R.color.white))
                        defaultButtonBackground.setStroke(1, ContextCompat.getColor(requireContext(), R.color.gray_A6A6A6))
                        btn.background = defaultButtonBackground
                        btn.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_A6A6A6))
                        btn.isSelected = false
                    }
                    val selectedButtonBackground = GradientDrawable()
                    selectedButtonBackground.shape = GradientDrawable.RECTANGLE
                    selectedButtonBackground.cornerRadius = 70f
                    selectedButtonBackground.setColor(ContextCompat.getColor(requireContext(), R.color.purple))
                    selectedButtonBackground.setStroke(3, ContextCompat.getColor(requireContext(), R.color.purple))
                    it.background = selectedButtonBackground
                    (it as Button).setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    it.isSelected = true

                    tvAdminareaH.text = secondWord
                    tvlocalityH.text = thirdWord

                    tvlocationText.text = "$thirdWord $fourthWord"
                    getHomeWeatherData(location.locationIndex)
                }
            }

            val params = LinearLayout.LayoutParams(
                220,
                80
            ).apply {
                setMargins(0, 0, 20, 0)
            }

            button.layoutParams = params

            if (index == 0) {
                firstButton = button // 첫 번째 버튼 저장
            }
            buttonContainer.addView(button)
        }

        // 첫 번째 버튼 자동 선택
        firstButton?.let {
            it.isSelected = true
            val selectedButtonBackground = GradientDrawable()
            selectedButtonBackground.shape = GradientDrawable.RECTANGLE
            selectedButtonBackground.cornerRadius = 20f
            selectedButtonBackground.setColor(ContextCompat.getColor(requireContext(), R.color.purple)) // 배경색
            selectedButtonBackground.setStroke(1, ContextCompat.getColor(requireContext(), R.color.purple)) // 테두리
            it.background = selectedButtonBackground
            it.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
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
                                clothesRecommendToHome.getRecommendedClothes(locationIndex,latitude,longitude)
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
