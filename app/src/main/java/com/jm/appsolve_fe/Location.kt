package com.jm.appsolve_fe

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Response
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jm.appsolve_fe.databinding.FragmentLocationBinding
import com.jm.appsolve_fe.databinding.SearchLocationDialogBinding
import retrofit2.Call
import java.util.Locale

class Location : Fragment() {


    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private var mList = ArrayList<LocationData>()
    private lateinit var adapter: LocationAdapter

    private lateinit var justtext: TextView

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //val view = inflater.inflate(R.layout.fragment_location, container, false)

        _binding = FragmentLocationBinding.inflate(inflater, container, false)

        val view = binding.root

        justtext = view.findViewById(R.id.just_text) // just_text를 초기화

        val toolbarTitle = view.findViewById<TextView>(R.id.tv_toolbar_title)
        toolbarTitle.text = "위치"

        // > 버튼 클릭 시 Home으로 이동
        val backBtn: AppCompatImageView = view.findViewById(R.id.backbtn)
        backBtn.setOnClickListener {
            // Home로 이동
            (activity as MainActivity).replaceFragment(Home())
        }

        val currentLocationLayout: LinearLayout = view.findViewById(R.id.currentlocationlayout)

        val upArrow: ImageView = view.findViewById(R.id.locationup)
        val downArrow: ImageView = view.findViewById(R.id.locationdown)

        if (currentLocationLayout.visibility == View.VISIBLE) {
            upArrow.visibility = View.GONE
            downArrow.visibility = View.GONE
        } else {
            upArrow.visibility = View.VISIBLE
            downArrow.visibility = View.VISIBLE
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectItemButton: ImageButton = view.findViewById(R.id.opendialog)
        selectItemButton.setOnClickListener {
            showBottomSheet()
        }
    }

    private fun showBottomSheet() {

        val dialogBinding = SearchLocationDialogBinding.inflate(layoutInflater)

        //val dialogView = layoutInflater.inflate(R.layout.search_location_dialog, null)
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        dialog.setContentView(dialogBinding.root)

        recyclerView = dialogBinding.recyclerView
        searchView = dialogBinding.searchView


        addLocToList()

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = LocationAdapter(mList)
        recyclerView.adapter = adapter

        adapter.itemClickListener = object : LocationAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val item = mList[position]
                Toast.makeText(context, "${item.address} 클릭", Toast.LENGTH_SHORT).show()
            }
        }


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
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

    private fun filterList(query: String?) {

        if (query != null) {
            val filteredList = ArrayList<LocationData>()
            for (i in mList) {
                if(i.address.contains(query.trim())){
                    filteredList.add(i)
                }
            }
            if (filteredList.isEmpty()){
                Toast.makeText(context, "No Data found", Toast.LENGTH_LONG).show()
            } else {
                adapter.setFilteredList(filteredList)
            }
        }
    }

    private fun addLocToList() {

        mList.add(LocationData("서울시 용산구 청파동"))
        mList.add(LocationData("서울시 용산구 뭐뭐동"))
        mList.add(LocationData("서울시 송파구 일이동"))
        mList.add(LocationData("서울시 마포구 일이동"))
        mList.add(LocationData("서울시 마포구 성암동"))
        mList.add(LocationData("서울시 용산구 청파동1"))
        mList.add(LocationData("서울시 용산구 뭐뭐동2"))
        mList.add(LocationData("서울시 송파구 일이동3"))
        mList.add(LocationData("서울시 마포구 일이동4"))
        mList.add(LocationData("서울시 마포구 성암동5"))

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
