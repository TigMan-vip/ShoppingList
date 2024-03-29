package com.example.shoppinglist.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.shoppinglist.domain.ShopItem

@Entity(tableName = "Shop_items")
data class ShopItemDbModel(
	@PrimaryKey(autoGenerate = true)
	var id: Int,
	val name: String,
	val count: Int,
	val enabled: Boolean,
)
