package com.jm.appsolve_fe.closet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jm.appsolve_fe.R
import com.jm.appsolve_fe.closet.data.ShoppingResponse

class ShoppingAdapter : RecyclerView.Adapter<ShoppingAdapter.ViewHolder>() {

    private var shoppingList: List<ShoppingResponse.ShoppingRecommendation> = emptyList()

    fun updateData(newList: List<ShoppingResponse.ShoppingRecommendation>) {
        shoppingList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shopping_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = shoppingList[position]
        holder.categoryText.text = item.category
        holder.productNameText.text = item.productName
        holder.mallNameText.text = "#${item.mallName}"
    }

    override fun getItemCount(): Int = shoppingList.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryText: TextView = view.findViewById(R.id.category_text)
        val productNameText: TextView = view.findViewById(R.id.product_name_text)
        val mallNameText: TextView = view.findViewById(R.id.mall_name_text)
    }
}
