package com.example.shoppinglist.Data

import com.example.shoppinglist.Domain.ShopItem
import com.example.shoppinglist.Domain.ShopListRepository

object ShopListRepositoryImpl:ShopListRepository {

	private val shopList = mutableListOf<ShopItem>()

	private var autoIncrementId = 0

	override fun addShopItem(shopItem: ShopItem) {
		if (shopItem.id == ShopItem.UNDEFINDED_ID) {
			shopItem.id = autoIncrementId++
		}
		shopList.add(shopItem)
	}

	override fun deleteShopItem(shopItem: ShopItem) {
		shopList.remove(shopItem)
	}

	override fun editShopItem(shopItem: ShopItem) {
		val oldElement = getShopItem(shopItem.id)
		shopList.remove(oldElement)
		addShopItem(shopItem)
	}

	override fun getShopItem(shopItemId: Int): ShopItem {
		return shopList.find { it.id == shopItemId } ?: throw java.lang.RuntimeException()
	}

	override fun getShopList(): List<ShopItem> {
		return shopList.toList()
	}
}