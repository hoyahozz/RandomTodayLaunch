package com.example.randomtodaylaunch.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.randomtodaylaunch.adapter.MenuAdapter
import com.example.randomtodaylaunch.adapter.RecyclerViewDecoration
import com.example.randomtodaylaunch.databinding.ActivityDetailBinding
import com.example.randomtodaylaunch.model.MenuEntity
import com.example.randomtodaylaunch.viewModel.ListViewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: ListViewModel by viewModels()
    private lateinit var menuList: List<MenuEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fname = intent.getStringExtra("fname").toString()

        Log.d("Detail Activity", "onCreate: $fname")

        viewModel.getMenuList(fname)

        viewModel.menuList.observe(this) {
            menuList = it

            if (menuList.isEmpty()) {
                Toast.makeText(this, "메뉴가 등록되어 있지 않아요 😭", Toast.LENGTH_SHORT).show()
            } else {
                val adapter = MenuAdapter(menuList)
                binding.rcvMenu.apply {
                    this.adapter = adapter
                    this.layoutManager = LinearLayoutManager(context)
                    this.addItemDecoration(RecyclerViewDecoration(5))
                }
            }
        }
    }
}