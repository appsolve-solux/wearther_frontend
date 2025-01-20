package com.jm.appsolve_fe

import android.content.Intent
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Home : Fragment() {

    private lateinit var dateText : TextView

    @RequiresApi(Build.VERSION_CODES.O)
    private val now: LocalDate = LocalDate.now()

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
            (activity as MainActivity).replaceFragment(Location())
        }

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
            (activity as MainActivity).replaceFragment(TodayRecommended())
        }






        return view
    }



}
