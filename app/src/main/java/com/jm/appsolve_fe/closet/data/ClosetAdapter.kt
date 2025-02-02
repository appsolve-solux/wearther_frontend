package com.jm.appsolve_fe.closet.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.jm.appsolve_fe.R

class ClosetAdapter(
    private var upperclothesList: List<Int>,
    private var lowerclothesList: List<Int>,
    private var otherclothesList: List<Int>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_UPPER = 0
        private const val TYPE_LOWER = 1
        private const val TYPE_OTHER = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position < upperclothesList.size -> TYPE_UPPER
            position < upperclothesList.size + lowerclothesList.size -> TYPE_LOWER
            else -> TYPE_OTHER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_UPPER -> UpperViewHolder(inflater.inflate(R.layout.closet_upper_clothes, parent, false))
            TYPE_LOWER -> LowerViewHolder(inflater.inflate(R.layout.closet_lower_clothes, parent, false))
            else -> OtherViewHolder(inflater.inflate(R.layout.closet_other_clothes, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UpperViewHolder -> {
                val upperItem = upperclothesList[position]
                holder.upperImageView.setImageResource(getUpperDrawable(upperItem))
            }
            is LowerViewHolder -> {
                val lowerIndex = position - upperclothesList.size
                val lowerItem = lowerclothesList[lowerIndex]
                holder.lowerImageView.setImageResource(getLowerDrawable(lowerItem))
            }
            is OtherViewHolder -> {
                val otherIndex = position - upperclothesList.size - lowerclothesList.size
                val otherItem = otherclothesList[otherIndex]
                holder.otherImageView.setImageResource(getOtherDrawable(otherItem))
            }
        }
    }

    override fun getItemCount(): Int {
        return upperclothesList.size + lowerclothesList.size + otherclothesList.size
    }

    // ✅ 데이터 업데이트 함수 추가
    fun updateData(newUppers: List<Int>, newLowers: List<Int>, newOthers: List<Int>) {
        upperclothesList = newUppers
        lowerclothesList = newLowers
        otherclothesList = newOthers
        notifyDataSetChanged()
    }

    // 🔹 ViewHolder 분리 (상의, 하의, 기타)
    class UpperViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val upperImageView: ImageView = itemView.findViewById(R.id.upper_item_icon)
    }

    class LowerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lowerImageView: ImageView = itemView.findViewById(R.id.lower_item_icon)
    }

    class OtherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val otherImageView: ImageView = itemView.findViewById(R.id.other_item_icon)
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
            else -> R.drawable.standard_image
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
            11 -> R.drawable.down_11
            12 -> R.drawable.down_12
            13 -> R.drawable.down_13
            else -> R.drawable.standard_image
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
            else -> R.drawable.standard_image
        }
    }
}