package com.jm.appsolve_fe

import android.content.Context
import android.location.Geocoder
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.jm.appsolve_fe.databinding.SearchLocationDialogBinding
import com.jm.appsolve_fe.retrofit.LWRetrofitClient
import com.jm.appsolve_fe.retrofit.PostBookmarkLocationRequest
import com.jm.appsolve_fe.retrofit.PostBookmarkLocationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class LocationBottomSheetDialog(
    private val bList: MutableList<BookmarkData>, // bList를 파라미터로 받음
    private val onLocationSelected: (LocationData, Pair<Double, Double>?) -> Unit
) {
    fun show(context: Context) {
        val dialogBinding = SearchLocationDialogBinding.inflate(LayoutInflater.from(context))
        val dialog = BottomSheetDialog(context, R.style.BottomSheetDialogTheme)
        dialog.setContentView(dialogBinding.root)

        val loclistrecyclerView = dialogBinding.recyclerView
        val searchView = dialogBinding.searchView

        val mList = addLocToList()

        loclistrecyclerView.setHasFixedSize(true)
        loclistrecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val locationListAdapter = LocationAdapter(mList)
        loclistrecyclerView.adapter = locationListAdapter

        locationListAdapter.itemClickListener = object : LocationAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val selectedLocation = mList[position]
                val latLng = getLatLngFromAddress(context, selectedLocation.address)
                // 선택된 위치를 서버로 전송
                latLng?.let { (latitude, longitude) ->
                    val locationInfo = selectedLocation.address
                    val currentLocationIndex = bList.size + 1  // bList에 기반한 위치 인덱스 (현재 즐겨찾기 크기 + 1)

                    // Retrofit 호출
                    postBookmarkLocation(context, locationInfo, currentLocationIndex, latitude, longitude)

                    onLocationSelected(selectedLocation, latLng)
                }

                dialog.dismiss()
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(mList, newText, locationListAdapter)
                return true
            }
        })

        val window = dialog.window
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window?.setGravity(Gravity.TOP)
        window?.setDimAmount(0f)

        dialog.show()
    }

    private fun addLocToList(): List<LocationData> {
        val locations = listOf(
            "대한민국 서울특별시 용산구 이촌동",
            "대한민국 서울특별시 용산구 원효로4가",
            "대한민국 서울특별시 용산구 한강로3가",
            "대한민국 서울특별시 용산구 한강로2가",
            "대한민국 서울특별시 용산구 한강로1가",
            "대한민국 서울특별시 용산구 신계동",
            "대한민국 서울특별시 용산구 문배동",
            "대한민국 서울특별시 용산구 원효로1가",
            "대한민국 서울특별시 용산구 청파동3가",
            "대한민국 서울특별시 용산구 청파동2가",
            "대한민국 서울특별시 용산구 청파동1가",
            "대한민국 서울특별시 용산구 서계동",
            "대한민국 서울특별시 용산구 동자동",
            "대한민국 경기도 용인시 수지구 동천동"
        )

        return locations.map { LocationData(it) }
    }

    private fun filterList(
        mList: List<LocationData>,
        query: String?,
        locationListAdapter: LocationAdapter
    ) {
        if (query != null) {
            val filteredList = mList.filter { it.address.contains(query.trim()) }
            locationListAdapter.setFilteredList(filteredList)
        }
    }

    private fun getLatLngFromAddress(context: Context, address: String): Pair<Double, Double>? {
        val geocoder = Geocoder(context, Locale.getDefault())
        return try {
            val addresses = geocoder.getFromLocationName(address, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                val location = addresses[0]
                Pair(location.latitude, location.longitude)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun postBookmarkLocation(
        context: Context,
        locationInfo: String,
        locationIndex: Int,
        latitude: Double,
        longitude: Double
    ) {

        val request = PostBookmarkLocationRequest(locationInfo, locationIndex, latitude, longitude)
        Log.d("Request", Gson().toJson(request))

        // 테스트용 토큰 삽입(text2)
        val token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxNCIsImlhdCI6MTczODMwNTYxNSwiZXhwIjoxNzQwODk3NjE1fQ.PtjUrqVubRue8a0fuhnRYNmmHM7oPq2KgLrjO1IaWP4"  // 테스트용 토큰
        Log.d("Token", "JWT Token: $token")

        if (token != null) {
            // Retrofit 호출
            LWRetrofitClient.service.postBookmarkLocation("Bearer $token", request).enqueue(object : Callback<PostBookmarkLocationResponse> {
                override fun onResponse(
                    call: Call<PostBookmarkLocationResponse>,
                    response: Response<PostBookmarkLocationResponse>
                ) {
                    Log.d("ResponseBody", "Response Body: ${Gson().toJson(response.body())}")

                    if (response.isSuccessful) {
                        val result = response.body()?.result
                        if (result != null) {
                            Toast.makeText(context, "성공, ${result.locationInfo}, 인덱스: ${result.locationIndex}", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "위치 저장 실패, 서버에서 응답을 받지 못했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("API Error", "Error: ${response.code()} - $errorBody")
                        Toast.makeText(context, "위치 저장 실패, 응답 코드: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<PostBookmarkLocationResponse>, t: Throwable) {
                    Log.e("NetworkError", "Error: ${t.localizedMessage}")  // 네트워크 오류 로그
                    Toast.makeText(context, "네트워크 오류: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(context, "토큰이 없습니다. 로그인 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
        }
    }
}