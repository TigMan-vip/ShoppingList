package com.example.shoppinglist.Presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.Domain.ShopItem
import com.example.shoppinglist.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ShopItemFragment : Fragment() {

	private lateinit var viewModel: ShopItemViewModel

	private lateinit var buttonSave: Button
	private lateinit var tilName: TextInputLayout
	private lateinit var tilCount: TextInputLayout
	private lateinit var etName: TextInputEditText
	private lateinit var etCount: TextInputEditText

	private var screenMode: String = MODE_UNKNOWN
	private var shopItemID: Int = ShopItem.UNDEFINDED_ID

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		parseParams()
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_shop_item, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
		initView(view)
		addTextChangeListeners()
		observeViewModel()
		launchRightMode()
	}

	companion object {

		private const val SCREEN_MODE = "extra_mode"
		private const val SHOP_ITEM_ID = "extra_shop_item_id"
		private const val MODE_EDIT = "mode_edit"
		private const val MODE_ADD = "mode_add"
		private const val MODE_UNKNOWN = ""

		fun newInstanceAddItem(): ShopItemFragment {
			return ShopItemFragment().apply {
				arguments = Bundle().apply {
					putString(SCREEN_MODE, MODE_ADD)
				}
			}
		}

		fun newInstanceEditItem(shopItemID: Int): ShopItemFragment {
			return ShopItemFragment().apply {
				arguments = Bundle().apply {
					putString(SCREEN_MODE, MODE_EDIT)
					putInt(SHOP_ITEM_ID, shopItemID)
				}
			}
		}
	}

	private fun addTextChangeListeners() {
		etName.addTextChangedListener(object : TextWatcher {
			override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

			override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
				viewModel.resetErrorInputName()
			}

			override fun afterTextChanged(p0: Editable?) {}
		})
		etCount.addTextChangedListener(object : TextWatcher {
			override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

			override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
				viewModel.resetErrorInputCount()
			}

			override fun afterTextChanged(p0: Editable?) {}
		})
	}

	private fun launchRightMode() {
		when (screenMode) {
			MODE_ADD  -> launchAddMode()
			MODE_EDIT -> launchEditMode()
		}
	}

	private fun parseParams() {
		val args = requireArguments()
		if (!args.containsKey(SCREEN_MODE)) {
			throw RuntimeException("Param screen mode is absent")
		}

		val mode = args.getString(SCREEN_MODE)
		if (mode != MODE_ADD && mode != MODE_EDIT) {
			throw RuntimeException("Unknown screen mode $mode")
		}

		screenMode = mode
		if (screenMode == MODE_EDIT) {
			if (!args.containsKey(SHOP_ITEM_ID)) {
				throw RuntimeException("Param shop item id is absent")
			}
			shopItemID = args.getInt(SHOP_ITEM_ID, ShopItem.UNDEFINDED_ID)
		}
	}

	private fun observeViewModel() {
		viewModel.errorInputNameLiveData.observe(viewLifecycleOwner) {
			val message = if (it) {
				"Ошибка ввода"
			} else {
				null
			}
			tilName.error = message
		}
		viewModel.errorInputCountLiveData.observe(viewLifecycleOwner) {
			val message = if (it) {
				"Ошибка ввода"
			} else {
				null
			}
			tilCount.error = message
		}
		viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
			activity?.onBackPressed()
		}
	}

	private fun initView(view: View) {
		with(view) {
			buttonSave = findViewById(R.id.btn_save)
			tilName = findViewById(R.id.shop_item_name_input_layout)
			tilCount = findViewById(R.id.shop_item_count_input_layout)
			etName = findViewById(R.id.shop_item_name_edit_text)
			etCount = findViewById(R.id.shop_item_count_edit_text)
		}
	}

	private fun launchEditMode() {
		viewModel.getShopItem(shopItemID)
		var item = viewModel.shopItem.value
		etName.setText(item?.name) ?: throw RuntimeException("Нет имени")
		etCount.setText(item?.count.toString())
		buttonSave.setOnClickListener {
			val name = etName.text.toString()
			val count = etCount.text.toString()
			viewModel.editShopItem(name, count)
		}
	}

	private fun launchAddMode() {
		buttonSave.setOnClickListener {
			val name = etName.text.toString()
			val count = etCount.text.toString()
			viewModel.addShopItem(name, count)
		}
	}
}