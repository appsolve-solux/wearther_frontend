package com.jm.appsolve_fe

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.jm.appsolve_fe.closet.ShoppingAdapter
import com.jm.appsolve_fe.closet.data.ShoppingResponse
import com.jm.appsolve_fe.closet.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClosetProductDetailActivity: AppCompatActivity() {

    private lateinit var shoppingAdapter: ShoppingAdapter
    private var shoppingData: List<ShoppingResponse.ShoppingItem> = listOf()
    private lateinit var tabLayout: TabLayout
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.closet_product_detail)  // Activity용 XML로 변경

        tabLayout = findViewById(R.id.tabLayout)
        recyclerView = findViewById(R.id.recyclerView)
        findViewById<ImageView>(R.id.back_button)?.setOnClickListener {
            finish() // 현재 액티비티 종료 (뒤로 가기)
        }

        setupRecyclerView()
        fetchShoppingData()
    }
    private fun setupRecyclerView() {
        shoppingAdapter = ShoppingAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = true
        recyclerView.adapter = shoppingAdapter
    }

    private fun fetchShoppingData() {
        val token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzM4Mzk1NDY2LCJleHAiOjE3NDA5ODc0NjZ9.1qrUGeY3p8kZ3g0hYjJxHZJx9DHf52wmG-bTeKujE28"
        Log.d("ClosetProductDetail", "API 호출 - 토큰: $token")

        RetrofitClient.instance.getShoppingData(token).enqueue(object : Callback<ShoppingResponse> {
            override fun onResponse(call: Call<ShoppingResponse>, response: Response<ShoppingResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.let {
                        if (it.result.isNullOrEmpty()) {
                            Toast.makeText(this@ClosetProductDetailActivity, "불러올 데이터가 없습니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            shoppingData = it.result
                            setupTabs()
                            updateRecyclerView(shoppingData[0].shoppingRecommendDtoList)
                        }
                    }
                } else {
                    Log.e("ClosetProductDetail", "API 응답 실패 - 응답 코드: ${response.code()}")
                    Toast.makeText(this@ClosetProductDetailActivity, "데이터 불러오기 실패 (응답 오류)", Toast.LENGTH_SHORT).show()
                    updateRecyclerView(emptyList())
                }
            }

            override fun onFailure(call: Call<ShoppingResponse>, t: Throwable) {
                Log.e("ClosetProductDetail", "API 호출 실패", t)
                Toast.makeText(this@ClosetProductDetailActivity, "네트워크 오류", Toast.LENGTH_SHORT).show()
                updateRecyclerView(emptyList())
            }
        })
    }

    private fun setupTabs() {
        tabLayout.removeAllTabs()
        val tasteLabels = mapOf(
            1 to "페미닌", 2 to "모던", 3 to "드뮤어", 4 to "올드머니",
            5 to "힙", 6 to "캐주얼", 7 to "고프코어", 8 to "Y2K"
        )

        if (shoppingData.isEmpty()) {
            tabLayout.addTab(tabLayout.newTab().setText("추천 없음"))
            return
        }

        val shoppingMap = shoppingData.associateBy({ it.tasteId }) { it.shoppingRecommendDtoList }

        shoppingMap.keys.sorted().forEach { tasteId ->
            val tab = tabLayout.newTab().setText(tasteLabels[tasteId] ?: "취향 $tasteId")
            tabLayout.addTab(tab)
        }

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
        shoppingAdapter.updateData(newList)
    }

    companion object {
        fun newInstance() = ClosetProductDetailActivity()
    }
}