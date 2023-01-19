package com.example.shoppinglist.Data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoppinglist.Domain.ShopItem
import com.example.shoppinglist.Domain.ShopListRepository
import kotlin.random.Random

object ShopListRepositoryImpl:ShopListRepository {

	private val shopListLiveData = MutableLiveData<List<ShopItem>>()
	private val shopList = sortedSetOf<ShopItem>({o1, o2 -> o1.id.compareTo(o2.id)})

	private var autoIncrementId = 0

	init {
		for (i in 0 until 100){
			val item = ShopItem(name = "Name $i", count = i, enabled = Random.nextBoolean())
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

	private fun updateShopList() {
		shopListLiveData.value = shopList.toList()
	}
}