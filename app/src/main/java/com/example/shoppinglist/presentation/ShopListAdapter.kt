package com.example.shoppinglist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.ItemShopDisabledBinding
import com.example.shoppinglist.databinding.ItemShopEnabledBinding

class ShopListAdapter : androidx.recyclerview.widget.ListAdapter<ShopItem, ShopListViewHolder>(ShopItemDiffCallback()) {

	var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
	var onShopItemClickListener: ((ShopItem) -> Unit)? = null

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopListViewHolder {
		val layout = when (viewType) {
			VIEW_TYPE_ENABLED  -> R.layout.item_shop_enabled
			VIEW_TYPE_DISABLED -> R.layout.item_shop_disabled
			else               -> throw java.lang.RuntimeException("$viewType")
		}
		val binding = DataBindingUtil.inflate<ViewDataBinding>(
			LayoutInflater.from(parent.context),
			layout,
			parent,
			false
		)

		return ShopListViewHolder(binding)
	}

	override fun onBindViewHolder(holder: ShopListViewHolder, position: Int) {
		val shopItem = getItem(position)
		val binding = holder.binding
		binding.root.setOnLongClickListener {
			onShopItemLongClickListener?.invoke(shopItem)
			true
		}
		binding.root.setOnClickListener {
			onShopItemClickListener?.invoke(shopItem)
		}
		when (binding) {
			is ItemShopEnabledBinding -> {
				binding.shopItemName.text = shopItem.name
				binding.shopItemCount.text = shopItem.count.toString()
			}
			is ItemShopDisabledBinding -> {
				binding.shopItemName.text = shopItem.name
				binding.shopItemCount.text = shopItem.count.toString()
			}
		}

	}

	override fun getItemViewType(position: Int): Int {
		val shopListItem = getItem(position)

		return if (shopListItem.enabled) {
			VIEW_TYPE_ENABLED
		} else {
			VIEW_TYPE_DISABLED
		}
	}

	companion object {

		const val VIEW_TYPE_ENABLED = 1
		const val VIEW_TYPE_DISABLED = 0

		const val MAX_POOL_SIZE = 15
	}
}