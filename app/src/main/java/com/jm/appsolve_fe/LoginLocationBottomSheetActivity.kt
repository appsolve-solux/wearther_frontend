package com.jm.appsolve_fe

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LoginLocationBottomSheetActivity : BottomSheetDialogFragment() {

    private lateinit var locationAdapter: LoginLocationAdapter
    private val fullLocationList = listOf(
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.login_bottom_sheet_location, container, false)

        val searchEditText = view.findViewById<EditText>(R.id.searchEditText)
        val recyclerView = view.findViewById<RecyclerView>(R.id.locationRecyclerView)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        locationAdapter = LoginLocationAdapter(fullLocationList)
        recyclerView.adapter = locationAdapter

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                val filteredList = fullLocationList.filter {
                    it.contains(query, ignoreCase = true)
                }
                locationAdapter.updateList(filteredList)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        return view
    }

}