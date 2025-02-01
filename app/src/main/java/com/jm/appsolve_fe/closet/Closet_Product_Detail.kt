package com.jm.appsolve_fe.closet

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.jm.appsolve_fe.R
import com.jm.appsolve_fe.closet.data.ShoppingResponse
import com.jm.appsolve_fe.closet.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Closet_Product_Detail : AppCompatActivity() {

    private lateinit var shoppingAdapter: ShoppingAdapter
    private var shoppingData: List<ShoppingResponse.ShoppingItem> = listOf()
    private lateinit var tabLayout: TabLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.closet_product_detail)

        // XML 뷰 요소 바인딩
        tabLayout = findViewById(R.id.tabLayout)
        toolbar = findViewById(R.id.toolbar) // 클래스 필드 초기화
        setSupportActionBar(toolbar)

        // 뒤로가기 버튼 설정
        toolbar.findViewById<ImageView>(R.id.back_button)?.setOnClickListener {
            finish()
        }

        // 리사이클러뷰 설정
        setupRecyclerView()
        fetchShoppingData()
    }

    private fun setupRecyclerView() {
        shoppingAdapter = ShoppingAdapter()
        recyclerView = findViewById(R.id.recyclerView) // RecyclerView 바인딩
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)  // ✅ 성능 최적화
        recyclerView.isNestedScrollingEnabled = true  // ✅ 스크롤 활성화
        recyclerView.adapter = shoppingAdapter  // ✅ Adapter 연결
    }
    private fun fetchShoppingData() {
        val token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzM4Mzk1NDY2LCJleHAiOjE3NDA5ODc0NjZ9.1qrUGeY3p8kZ3g0hYjJxHZJx9DHf52wmG-bTeKujE28"
        Log.d("ClosetProductDetail", "API 호출 - 토큰: $token")

        RetrofitClient.instance.getShoppingData(token).enqueue(object : Callback<ShoppingResponse> {
            override fun onResponse(call: Call<ShoppingResponse>, response: Response<ShoppingResponse>) {
                Log.d("ClosetProductDetail", "API 응답 코드: ${response.code()}")
                Log.d("ClosetProductDetail", "API 응답 바디: ${response.body()?.toString()}")
                Log.d("ClosetProductDetail", "API 에러 바디: ${response.errorBody()?.string()}")

                if (response.isSuccessful && response.body() != null) {
                    response.body()?.let {
                        if (it.result.isNullOrEmpty()) {
                            Toast.makeText(this@Closet_Product_Detail, "불러올 데이터가 없습니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            shoppingData = it.result
                            setupTabs()
                            updateRecyclerView(shoppingData[0].shoppingRecommendDtoList)
                        }
                    }
                } else {
                    Log.e("ClosetProductDetail", "API 응답 실패 - 응답 코드: ${response.code()}, 에러: ${response.errorBody()?.string()}")
                    Toast.makeText(this@Closet_Product_Detail, "데이터 불러오기 실패 (응답 오류)", Toast.LENGTH_SHORT).show()
                    updateRecyclerView(emptyList())  // ✅ API 실패 시 빈 리스트로 RecyclerView 업데이트
                }
            }

            override fun onFailure(call: Call<ShoppingResponse>, t: Throwable) {
                Log.e("ClosetProductDetail", "API 호출 실패", t)
                Toast.makeText(this@Closet_Product_Detail, "네트워크 오류", Toast.LENGTH_SHORT).show()
                updateRecyclerView(emptyList())  // ✅ 네트워크 실패 시 빈 리스트로 RecyclerView 업데이트
            }
        })
    }

    private fun setupTabs() {
        Log.d("ClosetProductDetail", "shoppingData 크기: ${shoppingData.size}")
        tabLayout.removeAllTabs()

        val tasteLabels = mapOf(
            1 to "페미닌",
            2 to "모던",
            3 to "드뮤어",
            4 to "올드머니",
            5 to "힙",
            6 to "캐주얼",
            7 to "고프코어",
            8 to "Y2K"
        )

        if (shoppingData.isEmpty()) {
            val defaultTab = tabLayout.newTab().setText("추천 없음")
            tabLayout.addTab(defaultTab)
            return
        }

        // ✅ List를 Map으로 변환
        val shoppingMap = shoppingData.associateBy({ it.tasteId }) { it.shoppingRecommendDtoList }

        // ✅ Map을 사용해서 탭 추가
        shoppingMap.keys.sorted().forEach { tasteId ->
            val tasteName = tasteLabels[tasteId] ?: "취향 ${tasteId}"
            val tab = tabLayout.newTab().setText(tasteName)
            tabLayout.addTab(tab)
        }

        // ✅ 첫 번째 탭 데이터 표시
        shoppingMap.keys.firstOrNull()?.let { firstTasteId ->
            updateRecyclerView(shoppingMap[firstTasteId] ?: emptyList())
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    val selectedTasteId = shoppingMap.keys.sorted().getOrNull(it.position)
                    if (selectedTasteId != null) {
                        updateRecyclerView(shoppingMap[selectedTasteId] ?: emptyList())
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun updateRecyclerView(newList: List<ShoppingResponse.ShoppingRecommendation>) {
        if (!::shoppingAdapter.isInitialized) {
            setupRecyclerView()  // ✅ Adapter가 초기화되지 않은 경우 설정
        }
        shoppingAdapter.updateData(newList)
    }

}