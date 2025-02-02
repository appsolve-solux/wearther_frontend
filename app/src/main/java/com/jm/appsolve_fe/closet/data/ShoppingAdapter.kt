package com.jm.appsolve_fe.closet

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jm.appsolve_fe.R
import com.jm.appsolve_fe.closet.data.ShoppingResponse

class ShoppingAdapter : RecyclerView.Adapter<ShoppingAdapter.ShoppingViewHolder>() {

    private var shoppingList: List<ShoppingResponse.ShoppingRecommendation> = listOf()

    class ShoppingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val shoppingImage: ImageView = itemView.findViewById(R.id.shoppingImage)
        val shoppingCategory: TextView = itemView.findViewById(R.id.shoppingCategory)
        val shoppingTitle: TextView = itemView.findViewById(R.id.shoppingTitle)
        val shoppingTag: TextView = itemView.findViewById(R.id.shoppingTag)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.shopping_item, parent, false)
        return ShoppingViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
        val item = shoppingList[position]

        // ✅ "upper", "lower", "other"를 "상의", "하의", "기타"로 변환
        val categoryText = when (item.category.lowercase()) {
            "upper" -> "상의"
            "lower" -> "하의"
            "other" -> "기타"
            else -> item.category // 변환되지 않는 경우 원래 값 유지
        }
        holder.shoppingCategory.text = categoryText

        holder.shoppingTitle.text = item.productName
        holder.shoppingTag.text = "#${item.mallName}"

        // ✅ mallName에 따라 적절한 drawable 이미지 설정
        val mallDrawable = getMallDrawable(item.mallName)
        holder.shoppingImage.setImageResource(mallDrawable)


        // ✅ 클릭하면 해당 URL로 이동
        holder.itemView.setOnClickListener {
            val context = it.context
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.productUrl))
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return shoppingList.size
    }

    fun updateData(newList: List<ShoppingResponse.ShoppingRecommendation>) {
        shoppingList = newList
        notifyDataSetChanged()
    }

    // ✅ mallName에 따라 Drawable 리소스 반환
    private fun getMallDrawable(mallName: String): Int {
        return when (mallName.lowercase()) {
            "지그재그" -> R.drawable.zigzag
            "무신사" -> R.drawable.musinsa
            "29cm" -> R.drawable._29cm
            "에이블리" -> R.drawable.ably
            else -> R.drawable.standard_image // 기본 이미지
        }
    }
}