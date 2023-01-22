package com.example.shoppinglist.Presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.Domain.ShopItem
import com.example.shoppinglist.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ShopItemActivity : AppCompatActivity() {

	private var screenMode = MODE_UNKNOWN
	private var shopItemID = ShopItem.UNDEFINDED_ID
	private lateinit var viewModel: ShopItemViewModel

	private lateinit var buttonSave: Button
	private lateinit var tilName: TextInputLayout
	private lateinit var tilCount: TextInputLayout
	private lateinit var etName: TextInputEditText
	private lateinit var etCount: TextInputEditText

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_shop_item)
		parseIntent()
		viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
		initView()
		when (screenMode) {
			MODE_ADD  -> launchAddMode()
			MODE_EDIT -> launchEditMode()
		}
	}

	companion object {

		private const val EXTRA_SCREEN_MODE = "extra_mode"
		private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
		private const val MODE_EDIT = "mode_edit"
		private const val MODE_ADD = "mode_add"
		private const val MODE_UNKNOWN = ""

		fun newIntentAddItem(context: Context): Intent {
			val intent = Intent(context, ShopItemActivity::class.java)
			intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
			return intent
		}

		fun newIntentEditItem(context: Context, shopItemId: Int): Intent {
			val intent = Intent(context, ShopItemActivity::class.java)
			intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
			intent.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
			return intent
		}
	}

	private fun parseIntent() {
		if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
			throw RuntimeException("Param screen mode is absent")
		}
		val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
		if (mode != MODE_EDIT && mode != MODE_ADD) {
			throw RuntimeException("Unknown screen mode $mode")
		}
		screenMode = mode
		if (screenMode == MODE_EDIT) {
			if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
				throw RuntimeException("Param shop item id is absent")
			}
			shopItemID = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINDED_ID)
		}
	}

	private fun initView() {
		buttonSave = findViewById(R.id.btn_save)
		tilName = findViewById(R.id.shop_item_name_input_layout)
		tilCount = findViewById(R.id.shop_item_count_input_layout)
		etName = findViewById(R.id.shop_item_name_edit_text)
		etCount = findViewById(R.id.shop_item_count_edit_text)
	}

	private fun launchEditMode() {
		viewModel.getShopItem(shopItemID)
		var item = viewModel.shopItem.value
		etName.setText(item?.name) ?: throw RuntimeException("Нет имени")
		etCount.setText(item?.count.toString())
		resetErrorToClickOnCount()
		resetErrorToClickOnName()
		buttonSave.setOnClickListener {
			val name = etName.text.toString()
			val count = etCount.text.toString()
			viewModel.editShopItem(name, count)

			viewModel.errorInputNameLiveData.observe(this) {
				if (it) {
					tilName.error = "Ошибка ввода"
				}
			}

			viewModel.errorInputCountLiveData.observe(this) {
				if (it) {
					tilCount.error = "Ошибка ввода"
				}
			}

			viewModel.resetErrorInputName()
			viewModel.resetErrorInputCount()

			viewModel.shouldCloseScreen.observe(this) {
				finish()
			}
		}
		resetErrorToClickOnName()
		resetErrorToClickOnCount()
	}

	private fun launchAddMode() {
		buttonSave.setOnClickListener {
			val name = etName.text.toString()
			val count = etCount.text.toString()
			viewModel.addShopItem(name, count)
			viewModel.errorInputNameLiveData.observe(this) {
				if (it) {
					tilName.error = "Ошибка ввода"
				}
			}

			viewModel.errorInputCountLiveData.observe(this) {
				if (it) {
					tilCount.error = "Ошибка ввода"
				}
			}

			viewModel.resetErrorInputName()
			viewModel.resetErrorInputCount()

			viewModel.shouldCloseScreen.observe(this) {
				finish()
			}
		}
		resetErrorToClickOnName()
		resetErrorToClickOnCount()
	}

	private fun resetErrorToClickOnName() {
		etName.addTextChangedListener(object : TextWatcher {
			override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

			override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
				tilName.error = null
			}

			override fun afterTextChanged(p0: Editable?) {}
		})
	}

	private fun resetErrorToClickOnCount() {
		etCount.addTextChangedListener(object : TextWatcher {
			override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

			override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
				tilCount.error = null
			}

			override fun afterTextChanged(p0: Editable?) {}
		})
	}
}