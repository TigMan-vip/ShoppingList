package com.example.shoppinglist.Presentation

import androidx.lifecycle.ViewModel
import com.example.shoppinglist.Data.ShopListRepositoryImpl
import com.example.shoppinglist.Domain.AddShopItemUseCase
import com.example.shoppinglist.Domain.EditShopItemUseCase
import com.example.shoppinglist.Domain.GetShopItemUseCase
import com.example.shoppinglist.Domain.ShopItem

class ShopItemViewModel: ViewModel() {

	private val repository = ShopListRepositoryImpl

	private val addShopItemUseCase = AddShopItemUseCase(repository)
	private val editShopItemUseCase = EditShopItemUseCase(repository)
	private val getShopItemUseCase = GetShopItemUseCase(repository)

	fun addShopItem(inputName: String?, inputCount: String?) {
		val name = parseName(inputName)
		val count = parseCount(inputCount)
		val fieldsValid = validateInput(name, count)
		val shopItem = ShopItem(name, count, true)
		if (fieldsValid) {
			addShopItemUseCase.addShopItem(shopItem)
		}
	}

	fun editShopItem(inputName: String?, inputCount: String?, enabled: Boolean) {
		val name = parseName(inputName)
		val count = parseCount(inputCount)
		val fieldsValid = validateInput(name, count)
		val shopItem = ShopItem(name, count, enabled)
		if (fieldsValid) {
			editShopItemUseCase.editShopItem(shopItem)
		}
	}

	fun getShopItem(shopItemId: Int) {

		getShopItemUseCase.getShopItem(shopItemId)
	}

	private fun parseName(inputName: String?): String {
		return inputName?.trim() ?: ""
	}

	private fun parseCount(inputCount: String?): Int {
		return try {
			inputCount?.trim()?.toInt() ?: 0
		} catch (e: Exception) {
			0
		}
	}

	private fun validateInput(name: String, count: Int): Boolean {
		var result = true
		if (name.isBlank()) {
			// TODO : show error input name
			result = false
		}
		if (count <= 0) {
			// TODO : show error input count
			result = false
		}
		return result
	}
}