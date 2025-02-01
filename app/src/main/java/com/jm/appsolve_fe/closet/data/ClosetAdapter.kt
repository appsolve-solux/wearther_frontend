package com.jm.appsolve_fe.closet.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.jm.appsolve_fe.R

class ClosetAdapter(private val upperclothesList: List<Int>, private val lowerclothesList: List<Int>, private val otherclothesList: List<Int>) : RecyclerView.Adapter<ClosetAdapter.ClosetViewHolder>() {
    class ClosetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.closet_item_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClosetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.closet_item_clothes, parent, false)
        return ClosetViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClosetViewHolder, position: Int) {
        val upperitem = upperclothesList[position]
        val loweritem = lowerclothesList[position]
        val otheritem = otherclothesList[position]

        val drawableResId1 = getUpperDrawable(upperitem)
        val drawableResId2 = getLowerDrawable(loweritem)
        val drawableResId3 = getOtherDrawable(otheritem)

        // 이미지 설정
        holder.imageView.setImageResource(drawableResId1)
        holder.imageView.setImageResource(drawableResId2)
        holder.imageView.setImageResource(drawableResId3)
    }

    override fun getItemCount(): Int {
        return upperclothesList.size
    }


    // ✅ 옷 ID에 따라 적절한 drawable 반환하는 함수
    private fun getUpperDrawable(itemId: Int): Int {
        return when (itemId) {
            1 -> R.drawable.up_01
            2 -> R.drawable.up_02
            3 -> R.drawable.up_03
            4 -> R.drawable.up_04
            5 -> R.drawable.up_05
            6 -> R.drawable.up_06
            7 -> R.drawable.up_07
            8 -> R.drawable.up_08
            9 -> R.drawable.up_09
            10 -> R.drawable.up_10
            11 -> R.drawable.up_11
            12 -> R.drawable.up_12
            13 -> R.drawable.up_13
            14 -> R.drawable.up_14
            15 -> R.drawable.up_15
            16 -> R.drawable.up_16
            17 -> R.drawable.up_17
            18 -> R.drawable.up_18
            19 -> R.drawable.up_19
            20 -> R.drawable.up_20
            21 -> R.drawable.up_21
            else -> R.drawable.ic_launcher_foreground // 기본 이미지
        }
    }

    private fun getLowerDrawable(itemId: Int): Int {
        return when (itemId) {
            1 -> R.drawable.down_01
            2 -> R.drawable.down_02
            3 -> R.drawable.down_03
            4 -> R.drawable.down_04
            5 -> R.drawable.down_05
            6 -> R.drawable.down_06
            7 -> R.drawable.down_07
            8 -> R.drawable.down_08
            9 -> R.drawable.down_09
            10 -> R.drawable.down_10
            else -> R.drawable.ic_launcher_foreground // 기본 이미지
        }
    }

    private fun getOtherDrawable(itemId: Int): Int {
        return when (itemId) {
            1 -> R.drawable.etc_01
            2 -> R.drawable.etc_02
            3 -> R.drawable.etc_03
            4 -> R.drawable.etc_04
            5 -> R.drawable.etc_05
            6 -> R.drawable.etc_06
            7 -> R.drawable.etc_07
            8 -> R.drawable.etc_08
            9 -> R.drawable.etc_09
            else -> R.drawable.ic_launcher_foreground // 기본 이미지
        }
    }

}
