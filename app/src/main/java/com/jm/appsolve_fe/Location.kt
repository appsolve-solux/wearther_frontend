package com.jm.appsolve_fe

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jm.appsolve_fe.databinding.FragmentLocationBinding
import com.jm.appsolve_fe.retrofit.GetBookmarkLocationResponse
import com.jm.appsolve_fe.retrofit.LWRetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Collections

class Location : Fragment() {

    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!

    // 즐겨찾기
    private lateinit var bookmarkrecyclerView: RecyclerView
    private var bList: ArrayList<BookmarkData> = ArrayList()
    private lateinit var bookmarkadapter: BookmarkAdapter

    private lateinit var currentLocationTemperature: TextView

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        val view = binding.root

        //툴바 타이틀 변경
        val toolbarTitle = view.findViewById<TextView>(R.id.tv_toolbar_title)
        toolbarTitle.text = "위치"

        // 툴바 >버튼 클릭 이벤트
        val backBtn: AppCompatImageView = view.findViewById(R.id.backbtn)
        backBtn.setOnClickListener {
            (activity as HomeMainActivity).replaceFragment(Home())
        }

        // 현재 위치 받아오기
        currentLocationTemperature = view.findViewById(R.id.tv_locationtemperature)
        val currentLocationLayout: LinearLayout = view.findViewById(R.id.currentlocationlayout)
        val getCurrentLocation = GetCurrentLocation(requireContext())
        getCurrentLocation.setCurrentLocationTemperature(currentLocationTemperature)
        // 위치 받아오는 부분
        getCurrentLocation.getCurrentLocation { latitude, longitude ->
            // 여기서 위치를 받아온 후 처리할 작업을 추가합니다.
            Log.d("Location", "위도: $latitude, 경도: $longitude")

            // 현재 위치의 온도를 받아와서 텍스트뷰에 설정
            getCurrentLocation.setCurrentLocationTemperature(currentLocationTemperature)
        }

        bookmarkrecyclerView = view.findViewById(R.id.bookmark_location_recyclerView)
        bookmarkrecyclerView.setHasFixedSize(true)
        bookmarkrecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        bookmarkadapter = BookmarkAdapter(bList) { position ->
            LocationDeleteDialog(requireContext(), position, bList, bookmarkadapter).show()
        }
        bookmarkrecyclerView.adapter = bookmarkadapter

        // 즐겨찾기 리사이클러뷰 drag
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,0) {
            override fun onMove(
                recyclerView: RecyclerView,
                source: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val sourcePosition = source.adapterPosition
                val targetPosition = target.adapterPosition

                Collections.swap(bList,sourcePosition,targetPosition)
                bookmarkadapter.notifyItemMoved(sourcePosition,targetPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                TODO("Not yet implemented")
            }

        })
        itemTouchHelper.attachToRecyclerView(bookmarkrecyclerView)

        // 위치 조정 화살표 visibility
        val upArrow: ImageView = view.findViewById(R.id.locationup)
        val downArrow: ImageView = view.findViewById(R.id.locationdown)

        if (currentLocationLayout.visibility == View.VISIBLE) {
            upArrow.visibility = View.GONE
            downArrow.visibility = View.GONE
        } else {
            upArrow.visibility = View.VISIBLE
            downArrow.visibility = View.VISIBLE
        }

        getBookmarkLocations()

        return view
    }

    private fun getBookmarkLocations() {
        val token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxNCIsImlhdCI6MTczODMxMDM4OCwiZXhwIjoxNzQwOTAyMzg4fQ.Pma0mIzOiSPvUto6Ya8qs1Cncoz5W2QBkOiZiGkiD40" // 토큰 예시

        LWRetrofitClient.service.getBookmarkLocation("Bearer $token")
            .enqueue(object : Callback<GetBookmarkLocationResponse> {
                override fun onResponse(
                    call: Call<GetBookmarkLocationResponse>,
                    response: Response<GetBookmarkLocationResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.d("Location", "API Response Successful: ${response.body()}")
                        val result = response.body()?.result
                        if (result != null && result.locations.isNotEmpty()) {
                            // 즐겨찾기 데이터 추가
                            for (location in result.locations) {
                                val addressParts = location.locationInfo.split(" ")
                                val newBookmark = BookmarkData(
                                    addressParts.getOrElse(1) { "" },
                                    addressParts.getOrElse(2) { "" },
                                    addressParts.getOrElse(3) { "" },
                                    location.temperature?.replace("°C", "°") ?: "N/A"
                                )
                                bList.add(newBookmark)
                            }
                            bookmarkadapter.notifyDataSetChanged()
                        } else {
                            Log.d("Location", "No Locations Found")
                            Toast.makeText(requireContext(), "위치 정보가 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e("Location", "API Response Error: ${response.code()} - ${response.message()}")
                        Toast.makeText(requireContext(), "서버 오류: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<GetBookmarkLocationResponse>, t: Throwable) {
                    Log.e("Location", "API 호출 실패: ${t.message}")
                    Toast.makeText(requireContext(), "API 호출 실패: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // dialog.show
        val openDialogButton: ImageButton =
            view.findViewById(R.id.opendialog)
        openDialogButton.setOnClickListener {
            LocationBottomSheetDialog(bList) { selectedLocation, latLng ->
                val addressParts = selectedLocation.address.split(" ")

                val newBookmark = BookmarkData(
                    addressParts.getOrElse(1) { "" },
                    addressParts.getOrElse(2) { "" },
                    addressParts.getOrElse(3) { "" },
                    "N/A"
                )

                bList.add(newBookmark)
                bookmarkadapter.notifyDataSetChanged()

                latLng?.let { (latitude, longitude) ->
                    /*.makeText(
                        requireContext(),
                        "위도: $latitude, 경도: $longitude",
                        Toast.LENGTH_SHORT
                    ).show()*/
                }
            }.show(requireContext())
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}