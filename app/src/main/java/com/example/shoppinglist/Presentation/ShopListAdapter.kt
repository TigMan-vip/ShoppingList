package com.example.shoppinglist.Presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.Domain.ShopItem
import com.example.shoppinglist.R

class ShopListAdapter: RecyclerView.Adapter<ShopListAdapter.ShopListViewHolder>() {

	var shopList = listOf<ShopItem>()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopListViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shop_enabled, parent,false)
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

	class ShopListViewHolder(val view: View): RecyclerView.ViewHolder(view) {
		val tvName = view.findViewById<TextView>(R.id.shop_item_name)
		val tvCount = view.findViewById<TextView>(R.id.shop_item_count)
	}
}