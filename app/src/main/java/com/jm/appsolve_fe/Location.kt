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
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jm.appsolve_fe.databinding.FragmentLocationBinding
import java.util.Locale

class Location : Fragment() {

    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!

    private var list = ArrayList<String>()


    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_location, container, false)

        _binding = FragmentLocationBinding.inflate(inflater, container, false)

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

        val selectItemButton: ImageButton = view.findViewById(R.id.tvSelectItem)
        selectItemButton.setOnClickListener {
            Log.d("LocationFragment", "TV Select Item clicked!")
            showBottomSheet()
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun showBottomSheet() {
        val dialogView = layoutInflater.inflate(R.layout.search_location_dialog, null)
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        dialog.setContentView(dialogView)

        val window = dialog.window
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            (resources.displayMetrics.heightPixels * 0.8).toInt()
        )
        window?.setGravity(Gravity.TOP)
        window?.setDimAmount(0f)

        /*val recyclerView: RecyclerView = dialogView.findViewById(R.id.rvItem)
        locationItemAdaptor = LocationItemAdaptor(list)
        recyclerView.adapter = locationItemAdaptor*/
        dialog.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
