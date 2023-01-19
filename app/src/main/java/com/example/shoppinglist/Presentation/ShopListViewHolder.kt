package com.example.shoppinglist.Presentation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R

class ShopListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

	val tvName = view.findViewById<TextView>(R.id.shop_item_name)
	val tvCount = view.findViewById<TextView>(R.id.shop_item_count)
}