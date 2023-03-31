package com.example.shoppinglist.domain

data class ShopItem(
	val name: String,
	val count: Int,
	val enabled: Boolean,
	var id: Int = UNDEFENDED_ID
){
	companion object {
		const val UNDEFENDED_ID = 0
	}
}