package com.example.shoppinglist.Domain

data class ShopItem(
	val name: String,
	val count: Int,
	val enabled: Boolean,
	var id: Int = ENDEFINDE_ID
){
	companion object {
		const val ENDEFINDE_ID = -1
	}
}