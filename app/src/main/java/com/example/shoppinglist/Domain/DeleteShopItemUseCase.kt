package com.example.shoppinglist.Domain

class DeleteShopItemUseCase(private val shopListRepository: ShopListRepository){

	fun deleteShopItem(shopItem: ShopItem) {
		shopListRepository.deleteShopItem(shopItem)
	}
}