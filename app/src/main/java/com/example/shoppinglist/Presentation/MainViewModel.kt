package com.example.shoppinglist.Presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppinglist.Data.ShopListRepositoryImpl
import com.example.shoppinglist.Domain.DeleteShopItemUseCase
import com.example.shoppinglist.Domain.EditShopItemUseCase
import com.example.shoppinglist.Domain.GetShopListUseCase
import com.example.shoppinglist.Domain.ShopItem
import com.example.shoppinglist.Domain.ShopListRepository

class MainViewModel: ViewModel() {

	val repository = ShopListRepositoryImpl

	val getShopListUseCase = GetShopListUseCase(repository)
	val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
	val editShopItemUseCase = EditShopItemUseCase(repository)

	val shoplist = getShopListUseCase.getShopList()

	fun deleteShopItem(shopItem: ShopItem) {
		deleteShopItemUseCase.deleteShopItem(shopItem)
	}

	fun changeEnableStateShopItem(shopItem: ShopItem) {
		val newState = shopItem.copy(enabled = !shopItem.enabled)
		editShopItemUseCase.editShopItem(newState)
	}
}