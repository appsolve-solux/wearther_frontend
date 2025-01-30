package com.jm.appsolve_fe

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
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

    //현재 위치 위경도
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

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

        //현재 위치 위경도,주소 받기
        val getCurrentLocation = GetCurrentLocation(requireContext())
        getCurrentLocation.getCurrentLocation()


        // 청파동 3가 버튼 클릭 시 TodayRecommendedActivity로 이동
        val locationBtn1: Button = view.findViewById(R.id.locationBtn1)
        locationBtn1.isSelected = true
        val locationBtn2: Button = view.findViewById(R.id.locationBtn2)
        val locationBtn3: Button = view.findViewById(R.id.locationBtn3)

        locationBtn1.setOnClickListener {
            locationBtn1.isSelected = !locationBtn1.isSelected

            locationBtn2.isSelected = false
            locationBtn3.isSelected = false
        }
        locationBtn2.setOnClickListener {
            // 버튼이 클릭되면 선택 상태를 변경
            locationBtn2.isSelected = !locationBtn2.isSelected

            locationBtn1.isSelected = false
            locationBtn3.isSelected = false
        }

        locationBtn3.setOnClickListener {
            // 버튼이 클릭되면 선택 상태를 변경
            locationBtn3.isSelected = !locationBtn3.isSelected

            locationBtn1.isSelected = false
            locationBtn2.isSelected = false
        }

        val clothesItemLayout: LinearLayout = view.findViewById(R.id.recommendClothes1)

        clothesItemLayout.setOnClickListener {
            // 다른 Fragment로 이동
            (activity as HomeMainActivity).replaceFragment(TodayRecommended())
        }
        
        // 시간별 날씨 리사이클러뷰
        hwrecyclerView = view.findViewById(R.id.home_weather_recyclerView)
        hwrecyclerView.setHasFixedSize(true)
        hwrecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        addData()
        hwadapter = HomeWeatherAdapter(wList)
        hwrecyclerView.adapter = hwadapter


        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addData() {

        val now = LocalDateTime.now()

        val hourlyTemp = listOf("4°", "3°", "2°", "2°", "1°", "0°", "-1°")
        val hourlySky = listOf("흐림", "흐림", "구름 많음", "맑음", "맑음", "맑음", "맑음")

        // 시간 증가에 따른 데이터 추가
        for (i in 0 until 7) {
            val currentHour = now.plusHours(i.toLong()) 
            val hourFormatted = currentHour.format(DateTimeFormatter.ofPattern("HH시")) // 시각 포맷
            val temperature = hourlyTemp.getOrElse(i) { "0°" }
            val sky = hourlySky.getOrElse(i) { "맑음" } 
            
            val weatherImage = when (sky) {
                "흐림" -> R.drawable.weather_cloudy
                "맑음" -> R.drawable.weather_sun_cloudy
                "구름 많음" -> R.drawable.weather_sun_cloudy
                else -> R.drawable.weather_cloudy
            }

            wList.add(HomeWeatherData(hourFormatted, weatherImage, temperature))
        }
    }
}
