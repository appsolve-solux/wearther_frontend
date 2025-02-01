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
import com.jm.appsolve_fe.closet.api.ClosetApi
import com.jm.appsolve_fe.closet.data.ShoppingResponse
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class ClosetTab01 : Fragment() {

    private lateinit var shoppingAdapter: ShoppingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.closet_tab01, container, false)

        val shoppingRecyclerView = view.findViewById<RecyclerView>(R.id.shoppingRecyclerView)
        shoppingRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        shoppingAdapter = ShoppingAdapter()
        shoppingRecyclerView.adapter = shoppingAdapter

        fetchShoppingRecommendations()

        return view
    }

    private fun fetchShoppingRecommendations() {
        val authToken = getAuthToken() // JWT 토큰 가져오기

        val retrofit = Retrofit.Builder()
            .baseUrl("http://54.180.114.64:8080/")
            .addConverterFactory(GsonConverterFactory.create())

            .client(OkHttpClient.Builder().addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $authToken") // ✅ JWT 추가
                    .build()
                chain.proceed(request)
            }.build())
            .build()

        val api = retrofit.create(ClosetApi::class.java)

        api.getShoppingRecommendations().enqueue(object : Callback<ShoppingResponse> {
            override fun onResponse(call: Call<ShoppingResponse>, response: Response<ShoppingResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val shoppingData = response.body()!!.result.flatMap { it.shoppingRecommendDtoList }

                    // 항상 4개의 항목만 유지하도록 조정
                    val fixedSizeList = shoppingData.take(4)
                    shoppingAdapter.updateData(fixedSizeList)
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("ShoppingAPI", "Error: $errorBody")
                    Toast.makeText(requireContext(), "Failed to load shopping recommendations", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ShoppingResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun getAuthToken(): String {
        val sharedPref = requireActivity().getSharedPreferences("AppPreferences", android.content.Context.MODE_PRIVATE)
        val token = sharedPref.getString("JWT_TOKEN", "") ?: ""
        Log.d("JWT_DEBUG", "현재 사용 중인 JWT: $token") // ✅ 현재 JWT 로그 출력
        return token
    }
    private fun saveAuthToken(token: String) {
        val sharedPref = requireActivity().getSharedPreferences("AppPreferences", android.content.Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("JWT_TOKEN", token)
            apply()  // ✅ 비동기 저장 (commit() 사용 가능)
        }
        Log.d("JWT_DEBUG", "JWT 저장됨: $token")
    }
}
