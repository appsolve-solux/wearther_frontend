package com.jm.appsolve_fe

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.SearchView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jm.appsolve_fe.databinding.SearchLocationDialogBinding

class LocationBottomSheetDialog(
    private val onLocationSelected: (LocationData) -> Unit
) {

    fun show(context: Context) {
        val dialogBinding = SearchLocationDialogBinding.inflate(LayoutInflater.from(context))
        val dialog = BottomSheetDialog(context, R.style.BottomSheetDialogTheme)
        dialog.setContentView(dialogBinding.root)

        val loclistrecyclerView = dialogBinding.recyclerView
        val searchView = dialogBinding.searchView

        // 위치 목록 추가
        val mList = addLocToList()

        loclistrecyclerView.setHasFixedSize(true)
        loclistrecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val locationListAdapter = LocationAdapter(mList)
        loclistrecyclerView.adapter = locationListAdapter

        locationListAdapter.itemClickListener = object : LocationAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val selectedLocation = mList[position]
                onLocationSelected(selectedLocation)

                dialog.dismiss()
            }
        }

        // 검색 기능
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
            "대한민국 서울특별시 용산구 동자동"
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
            if (filteredList.isEmpty()) {
                // Toast.makeText(context, "No Data found", Toast.LENGTH_LONG).show()
            } else {
                locationListAdapter.setFilteredList(filteredList)
            }
        }
    }
}