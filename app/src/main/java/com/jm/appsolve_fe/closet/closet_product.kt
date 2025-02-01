package com.jm.appsolve_fe.closet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.jm.appsolve_fe.R
import com.jm.appsolve_fe.closet.api.ClosetApi
import com.jm.appsolve_fe.closet.data.ClosestResponseWrapper
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class ClosetProduct : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 프래그먼트 레이아웃을 inflate
        val view = inflater.inflate(R.layout.closet_product, container, false)

        // ImageView 참조
        val imageClothesProduct = view.findViewById<ImageView>(R.id.product_clothes01)

        // Retrofit 초기화
        val retrofit = Retrofit.Builder()
            .baseUrl("http://54.180.114.64:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()

        val api = retrofit.create(ClosetApi::class.java)

        // API 호출
        api.getRecommendedCloset().enqueue(object : Callback<ClosestResponseWrapper> {
            override fun onResponse(call: Call<ClosestResponseWrapper>, response: Response<ClosestResponseWrapper>) {
                if (response.isSuccessful) {
                    val data = response.body()?.result
                    if (data != null && data.uppers.isNotEmpty()) {
                        val imageUrl = data.uppers[0] // 첫 번째 상의 이미지 URL 사용

                        // Glide를 사용하여 ImageView에 이미지 로드
                        Glide.with(requireContext())
                            .load(imageUrl)
                            .into(imageClothesProduct)
                    } else {
                        Toast.makeText(requireContext(), "No data available", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ClosestResponseWrapper>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

        // 클릭 리스너에서 데이터 전달
        imageClothesProduct.setOnClickListener {
            val intent = Intent(requireContext(), Closet_Product_Detail::class.java)
            intent.putExtra("clothesName", "추천 상품 1") // 이름 전달
            startActivity(intent)
        }

        return view
    }
}