package com.example.shoppinglist.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.databinding.FragmentShopItemBinding

class ShopItemFragment : Fragment() {

	private lateinit var viewModel: ShopItemViewModel

	private var _binding: FragmentShopItemBinding? = null
	private val binding: FragmentShopItemBinding
		get() = _binding ?: throw RuntimeException("ShopItemFragmentBinding = null")

	private var screenMode: String = MODE_UNKNOWN
	private var shopItemID: Int = ShopItem.UNDEFENDED_ID
	private lateinit var onEditingFinishedListener: OnEditingFinishedListener

	override fun onAttach(context: Context) {
		super.onAttach(context)
		Log.d("ShopItemFragment", "onAttach")
		if (context is OnEditingFinishedListener) {
			onEditingFinishedListener = context
		} else {
			throw RuntimeException("Activity must implement OnEditingFinishedListener")
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		Log.d("ShopItemFragment", "onCreate")
		parseParams()
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentShopItemBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
		binding.viewModel = viewModel
		binding.lifecycleOwner = viewLifecycleOwner
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

	interface OnEditingFinishedListener {

		fun onEditingFinished()
	}

	private fun addTextChangeListeners() {
		binding.shopItemNameEditText.addTextChangedListener(object : TextWatcher {
			override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

			override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
				viewModel.resetErrorInputName()
			}

			override fun afterTextChanged(p0: Editable?) {}
		})
		binding.shopItemCountEditText.addTextChangedListener(object : TextWatcher {
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
			shopItemID = args.getInt(SHOP_ITEM_ID, ShopItem.UNDEFENDED_ID)
		}
	}

	private fun observeViewModel() {
		viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
			onEditingFinishedListener.onEditingFinished()
		}
	}

	private fun launchEditMode() {
		viewModel.getShopItem(shopItemID)
		binding.btnSave.setOnClickListener {
			val name = binding.shopItemNameEditText.text.toString()
			val count = binding.shopItemCountEditText.text.toString()
			viewModel.editShopItem(name, count)

		}
	}

	private fun launchAddMode() {

		binding.btnSave.setOnClickListener {
			val name = binding.shopItemNameEditText.text.toString()
			val count = binding.shopItemCountEditText.text.toString()
			viewModel.addShopItem(name, count)

		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}