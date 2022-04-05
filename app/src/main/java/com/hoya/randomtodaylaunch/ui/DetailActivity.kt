package com.hoya.randomtodaylaunch.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoya.randomtodaylaunch.adapter.MenuAdapter
import com.hoya.randomtodaylaunch.util.RecyclerViewDecoration
import com.hoya.randomtodaylaunch.databinding.ActivityDetailBinding
import com.hoya.randomtodaylaunch.viewModel.ListViewModel

/* 음식점 상세 정보 액티비티 */
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: ListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModelFactory = ListViewModel.ListViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory).get(ListViewModel::class.java)

        val fname = intent.getStringExtra("fname").toString()
        val adapter = MenuAdapter()

        binding.rcvMenu.apply {
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(context)
            this.addItemDecoration(RecyclerViewDecoration(5))
        }

        viewModel.getMenuList(fname).observe(this) {
            adapter.submitList(it)
            if (it.isEmpty()) {
                Toast.makeText(this, "메뉴가 등록되어 있지 않아요 😭", Toast.LENGTH_SHORT).show()
            }
        }
    }
}