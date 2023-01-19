package com.example.shoppinglist.Presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.Domain.ShopItem
import com.example.shoppinglist.R

class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ShopListViewHolder>() {

	var shopList = listOf<ShopItem>()

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
		val shopItem = shopList[position]
		holder.tvName.text = shopItem.name
		holder.tvCount.text = shopItem.count.toString()
		holder.view.setOnLongClickListener {
			true
		}
	}

	override fun getItemCount(): Int {
		return shopList.size
	}

	override fun getItemViewType(position: Int): Int {
		val shopListItem = shopList[position]

		return if (shopListItem.enabled) {
			VIEW_TYPE_ENABLED
		} else {
			VIEW_TYPE_DISABLED
		}
	}

	class ShopListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

		val tvName = view.findViewById<TextView>(R.id.shop_item_name)
		val tvCount = view.findViewById<TextView>(R.id.shop_item_count)
	}

	companion object {

		const val VIEW_TYPE_ENABLED = 1
		const val VIEW_TYPE_DISABLED = 0

		const val MAX_POOL_SIZE = 15
	}
}