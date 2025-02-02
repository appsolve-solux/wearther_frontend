package com.jm.appsolve_fe

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TodayRecommended : Fragment() {

    private lateinit var todayRecommendImg1: ImageView
    private lateinit var todayRecommendText1: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_today_recommended, container, false)

        // > 버튼 클릭 시 Home으로 이동
        val backBtn: AppCompatImageView = view.findViewById(R.id.backbtn)
        backBtn.setOnClickListener {
            // Home로 이동
            (activity as HomeMainActivity).replaceFragment(Home())
        }

        val moreRecommendedBtn: Button = view.findViewById(R.id.moreRecommendBtn)
        moreRecommendedBtn.setOnClickListener {
            // Home로 이동
            (activity as HomeMainActivity).replaceFragment(ClosetProductDetail())
        }

        todayRecommendImg1 = view.findViewById(R.id.todayrecommendclothesimg1)
        todayRecommendText1 = view.findViewById(R.id.todayrecommendclothestext1)

        val receivedBitmap: Bitmap? = arguments?.getParcelable("imageBitmap")

        if (receivedBitmap != null) {
            todayRecommendImg1.setImageBitmap(receivedBitmap)
        } else {
            Log.e("ImageTransfer", "전달된 이미지가 없습니다.")
        }
        val recommendText = arguments?.getString("recommendText")
        if (recommendText != null) {
            todayRecommendText1.text = recommendText
        } else {
            Log.e("TextTransfer", "전달된 텍스트가 없습니다.")
        }


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Apply WindowInsets
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, 0, 0 ,0)
            insets
        }
    }
}