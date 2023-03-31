package com.example.shoppinglist.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.data.ShopListRepositoryImpl
import com.example.shoppinglist.domain.AddShopItemUseCase
import com.example.shoppinglist.domain.EditShopItemUseCase
import com.example.shoppinglist.domain.GetShopItemUseCase
import com.example.shoppinglist.domain.ShopItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class ShopItemViewModel(application: Application) : AndroidViewModel(application) {

	private val repository = ShopListRepositoryImpl(application)

	private val addShopItemUseCase = AddShopItemUseCase(repository)
	private val editShopItemUseCase = EditShopItemUseCase(repository)
	private val getShopItemUseCase = GetShopItemUseCase(repository)

	private val _errorInputNameLiveData = MutableLiveData<Boolean>()
	val errorInputNameLiveData: LiveData<Boolean>
		get() = _errorInputNameLiveData

	private val _errorInputCountLiveData = MutableLiveData<Boolean>()
	val errorInputCountLiveData: LiveData<Boolean>
		get() = _errorInputCountLiveData

	private val _shopItem = MutableLiveData<ShopItem>()
	val shopItem: LiveData<ShopItem>
		get() = _shopItem

	private val _shouldCloseScreen = MutableLiveData<Unit>()
	val shouldCloseScreen: LiveData<Unit>
		get() = _shouldCloseScreen



	fun addShopItem(inputName: String?, inputCount: String?) {
		viewModelScope.launch {
			val name = parseName(inputName)
			val count = parseCount(inputCount)
			val fieldsValid = validateInput(name, count)
			val shopItem = ShopItem(name, count, true)
			if (fieldsValid) {
				addShopItemUseCase.addShopItem(shopItem)
				finishWork()
			}
		}
	}

	fun editShopItem(inputName: String?, inputCount: String?) {
		viewModelScope.launch {
			val name = parseName(inputName)
			val count = parseCount(inputCount)
			val fieldsValid = validateInput(name, count)
			if (fieldsValid) {
				_shopItem.value?.let {
					val item = it.copy(name = name, count = count)
					editShopItemUseCase.editShopItem(item)
					finishWork()
				}
			}
		}
	}

	fun getShopItem(shopItemId: Int) {
		viewModelScope.launch {
			val item = getShopItemUseCase.getShopItem(shopItemId)
			_shopItem.value = item
		}
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
			_errorInputNameLiveData.value = true
			result = false
		}
		if (count <= 0) {
			_errorInputCountLiveData.value = true
			result = false
		}
		return result
	}

	fun resetErrorInputName() {
		_errorInputNameLiveData.value = false
	}

	fun resetErrorInputCount() {
		_errorInputCountLiveData.value = false
	}

	private fun finishWork() {
		_shouldCloseScreen.value = Unit
	}
}