package com.jm.appsolve_fe.closet.data

data class ShoppingResponse(
    val httpStatus: String,
    val success: Boolean,
    val result: List<ShoppingItem>,
    val error: String?
)

{
    data class ShoppingItem(
        val tasteId: Int,
        val shoppingRecommendDtoList: List<ShoppingRecommendation>
    )

    data class ShoppingRecommendation(
        val category: String,
        val wear_id: Int,
        val productName: String,
        val productUrl: String,
        val mallName: String
    )
}
