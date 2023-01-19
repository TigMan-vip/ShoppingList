package com.example.shoppinglist.Data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoppinglist.Domain.ShopItem
import com.example.shoppinglist.Domain.ShopListRepository

object ShopListRepositoryImpl:ShopListRepository {

	private val shopListLiveData = MutableLiveData<List<ShopItem>>()
	private val shopList = mutableListOf<ShopItem>()

	private var autoIncrementId = 0

	init {
		for (i in 0 until 10){
			val item = ShopItem(name = "Name $i", count = i, enabled = true)
			addShopItem(item)
		}
	}

	override fun addShopItem(shopItem: ShopItem) {
		if (shopItem.id == ShopItem.UNDEFINDED_ID) {
			shopItem.id = autoIncrementId++
		}
		shopList.add(shopItem)
		updateShopList()
	}

	override fun deleteShopItem(shopItem: ShopItem) {
		shopList.remove(shopItem)
		updateShopList()
	}

	override fun editShopItem(shopItem: ShopItem) {
		val oldElement = getShopItem(shopItem.id)
		shopList.remove(oldElement)
		addShopItem(shopItem)
	}

	override fun getShopItem(shopItemId: Int): ShopItem {
		return shopList.find { it.id == shopItemId } ?: throw java.lang.RuntimeException()
	}

	override fun getShopList(): LiveData<List<ShopItem>> {
		return shopListLiveData
	}

	fun updateShopList() {
		shopListLiveData.value = shopList.toList()
	}
}