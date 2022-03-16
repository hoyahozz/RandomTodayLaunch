package com.hoya.randomtodaylaunch.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoya.randomtodaylaunch.adapter.ListAdapter
import com.hoya.randomtodaylaunch.util.RecyclerViewDecoration
import com.hoya.randomtodaylaunch.databinding.ActivityListBinding
import com.hoya.randomtodaylaunch.viewModel.ListViewModel


/* 음식점 리스트 액티비티 */
class ListActivity : AppCompatActivity() {

    private lateinit var binding : ActivityListBinding
    private val viewModel : ListViewModel by viewModels()
    private lateinit var adapter : ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ListAdapter(this)
        binding.listRcv.adapter = adapter
        binding.listRcv.layoutManager = LinearLayoutManager(this)
        binding.listRcv.addItemDecoration(RecyclerViewDecoration(5))

        viewModel.allFood.observe(this) {
            Log.d("ListAct", "$it")
            adapter.submitList(it)
        }
    }
}