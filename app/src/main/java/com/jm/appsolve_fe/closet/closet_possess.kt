package com.jm.appsolve_fe.closet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jm.appsolve_fe.R
import com.jm.appsolve_fe.closet.network.RetrofitClient
import com.jm.appsolve_fe.closet.data.ClosestResponseWrapper
import com.jm.appsolve_fe.closet.data.ClosetAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClosetPossess : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.closet_possess, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // ✅ 엑세스 토큰 사용하여 데이터 요청
        val accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzM4Mzk1NDY2LCJleHAiOjE3NDA5ODc0NjZ9.1qrUGeY3p8kZ3g0hYjJxHZJx9DHf52wmG-bTeKujE28"
        Log.d("ClosetPossess", "Fetching closet data with token:")
        fetchClosetData(accessToken)

        return view
    }
    private fun fetchClosetData(token: String) {
        Log.d("ClosetPossess", "Fetching closet data with token: $token")

        RetrofitClient.instance.getClosetData("Bearer $token") // ✅ Bearer 추가
            .enqueue(object : Callback<ClosestResponseWrapper> {
                override fun onResponse(call: Call<ClosestResponseWrapper>, response: Response<ClosestResponseWrapper>) {
                    if (response.isSuccessful) {
                        val data = response.body()?.result
                        Log.d("ClosetPossess", "Fetched data: $data")

                        if (data != null) {
                            val upperItems = data.uppers
                            val lowerItems = data.lowers
                            val otherItems = data.others

                            recyclerView.adapter = ClosetAdapter(upperItems, lowerItems, otherItems)
                        } else {
                            Log.e("ClosetPossess", "No data found")
                            Toast.makeText(requireContext(), "No data found", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e("ClosetPossess", "Failed to fetch data: ${response.errorBody()?.string()}")
                        Toast.makeText(requireContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ClosestResponseWrapper>, t: Throwable) {
                    Log.e("ClosetPossess", "Error fetching data: ${t.message}")
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}