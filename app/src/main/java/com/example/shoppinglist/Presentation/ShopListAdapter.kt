package com.example.shoppinglist.Presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.shoppinglist.Domain.ShopItem
import com.example.shoppinglist.R

class ShopListAdapter : androidx.recyclerview.widget.ListAdapter<ShopItem, ShopListViewHolder>(ShopItemDiffCallback()) {

	var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
	var onShopItemClickListener: ((ShopItem) -> Unit)? = null

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopListViewHolder {
		val layout = when (viewType) {
			VIEW_TYPE_ENABLED  -> R.layout.item_shop_enabled
			VIEW_TYPE_DISABLED -> R.layout.item_shop_disabled
			else               -> throw java.lang.RuntimeException("$viewType")
		}
		val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)

		return ShopListViewHolder(view)
	}

	override fun onBindViewHolder(holder: ShopListViewHolder, position: Int) {
		val shopItem = getItem(position)
		holder.view.setOnLongClickListener {
			onShopItemLongClickListener?.invoke(shopItem)
			true
		}
		holder.view.setOnClickListener {
			onShopItemClickListener?.invoke(shopItem)
			true
		}
		holder.tvName.text = shopItem.name
		holder.tvCount.text = shopItem.count.toString()
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