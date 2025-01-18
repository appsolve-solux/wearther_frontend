package com.jm.appsolve_fe.closet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.jm.appsolve_fe.R

class ClosetProduct : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 프래그먼트 레이아웃을 inflate
        val view = inflater.inflate(R.layout.closet_product, container, false)

        val imageClothesProduct = view.findViewById<ImageView>(R.id.product_clothes01)

        imageClothesProduct.setOnClickListener {
            val intent = Intent(requireContext(), closet_product_detail::class.java)
            intent.putExtra("clothesName", "추천 상품 1") // 데이터 전달
            startActivity(intent)
        }

        return view
    }
}