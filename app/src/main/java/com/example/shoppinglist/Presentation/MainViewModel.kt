package com.example.shoppinglist.Presentation

import androidx.lifecycle.ViewModel
import com.example.shoppinglist.Data.ShopListRepositoryImpl
import com.example.shoppinglist.Domain.DeleteShopItemUseCase
import com.example.shoppinglist.Domain.EditShopItemUseCase
import com.example.shoppinglist.Domain.GetShopListUseCase
import com.example.shoppinglist.Domain.ShopItem

class MainViewModel : ViewModel() {

	private val repository = ShopListRepositoryImpl

	private val getShopListUseCase = GetShopListUseCase(repository)
	private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
	private val editShopItemUseCase = EditShopItemUseCase(repository)

	val shopList = getShopListUseCase.getShopList()

	fun deleteShopItem(shopItem: ShopItem) {
		deleteShopItemUseCase.deleteShopItem(shopItem)
	}

	fun changeEnableStateShopItem(shopItem: ShopItem) {
		val newState = shopItem.copy(enabled = !shopItem.enabled)
		editShopItemUseCase.editShopItem(newState)
	}
}