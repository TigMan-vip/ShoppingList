package com.example.shoppinglist.Presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R

class MainActivity : AppCompatActivity() {

	private lateinit var viewModel: MainViewModel
	private lateinit var shopListAdapter: ShopListAdapter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setUpRecyclerView()
		viewModel = ViewModelProvider(this)[MainViewModel::class.java]
		viewModel.shoplist.observe(this) {
			shopListAdapter.shopList = it
		}

	}

	private fun setUpRecyclerView() {
		val rvShopList = findViewById<RecyclerView>(R.id.rv_shop_list)
		shopListAdapter = ShopListAdapter()

		with(rvShopList) {
			adapter = shopListAdapter
			recycledViewPool.setMaxRecycledViews(
				ShopListAdapter.VIEW_TYPE_ENABLED,
				ShopListAdapter.MAX_POOL_SIZE
			)
			recycledViewPool.setMaxRecycledViews(
				ShopListAdapter.VIEW_TYPE_ENABLED,
				ShopListAdapter.MAX_POOL_SIZE
			)
		}


	}
}