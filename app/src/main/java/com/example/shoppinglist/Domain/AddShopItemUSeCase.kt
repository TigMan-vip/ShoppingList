package com.example.shoppinglist.Domain

class AddShopItemUSeCase(private val shopListRepository: ShopListRepository) {

	fun addShopItem(shopItem: ShopItem) {
		shopListRepository.addShopItem(shopItem)
	}
}