package com.example.randomtodaylaunch.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.randomtodaylaunch.adapter.ListAdapter
import com.example.randomtodaylaunch.util.RecyclerViewDecoration
import com.example.randomtodaylaunch.databinding.ActivityListBinding
import com.example.randomtodaylaunch.viewModel.ListViewModel


/* 음식점 리스트 액티비티 */
class ListActivity : AppCompatActivity() {

    private lateinit var binding : ActivityListBinding
    private val viewModel : ListViewModel by viewModels()
    private lateinit var adapter : ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.allFood.observe(this) {
            adapter = ListAdapter(it, this)
            binding.listRcv.adapter = adapter
            binding.listRcv.layoutManager = LinearLayoutManager(this)
            binding.listRcv.addItemDecoration(RecyclerViewDecoration(5))
        }
    }
}