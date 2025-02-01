package com.jm.appsolve_fe.closet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jm.appsolve_fe.R
import com.jm.appsolve_fe.closet.network.RetrofitClient
import com.jm.appsolve_fe.closet.data.ClosestResponseWrapper
import com.jm.appsolve_fe.closet.data.ClosetAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClosetProduct : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var closetAdapter: ClosetAdapter // Adapter 선언

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.closet_product, container, false)

        recyclerView = view.findViewById(R.id.recyclerView2)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        // ⚠️ 빈 데이터로 초기 Adapter 설정 후 API 응답에서 업데이트
        closetAdapter = ClosetAdapter(emptyList(), emptyList(), emptyList())
        recyclerView.adapter = closetAdapter

        // ✅ 엑세스 토큰 사용하여 데이터 요청
        val accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzM4Mzk1NDY2LCJleHAiOjE3NDA5ODc0NjZ9.1qrUGeY3p8kZ3g0hYjJxHZJx9DHf52wmG-bTeKujE28"
        Log.d("ClosetProduct", "Fetching closet data with token: $accessToken")
        fetchClosetData(accessToken)

        return view
    }

    private fun fetchClosetData(token: String) {
        Log.d("ClosetProduct", "Fetching closet data with token: $token")

        RetrofitClient.instance.getRecommendedCloset("Bearer $token")
            .enqueue(object : Callback<ClosestResponseWrapper> {
                override fun onResponse(call: Call<ClosestResponseWrapper>, response: Response<ClosestResponseWrapper>) {
                    if (response.isSuccessful) {
                        val data = response.body()?.result
                        Log.d("ClosetProduct", "Fetched data: $data")

                        if (data != null) {
                            val upperItems = data.uppers ?: emptyList()
                            val lowerItems = data.lowers ?: emptyList()
                            val otherItems = data.others ?: emptyList()

                            // ⚠️ API 응답을 받아 기존 Adapter를 업데이트
                            closetAdapter.updateData(upperItems, lowerItems, otherItems)
                        } else {
                            Log.e("ClosetProduct", "No data found")
                            Toast.makeText(requireContext(), "No data found", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e("ClosetProduct", "Failed to fetch data: ${response.errorBody()?.string()}")
                        Toast.makeText(requireContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ClosestResponseWrapper>, t: Throwable) {
                    Log.e("ClosetProduct", "Error fetching data: ${t.message}")
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}