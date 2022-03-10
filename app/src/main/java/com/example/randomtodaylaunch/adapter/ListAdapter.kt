package com.example.randomtodaylaunch.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.view.Display
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.RecyclerView
import com.example.randomtodaylaunch.R
import com.example.randomtodaylaunch.databinding.ItemListBinding
import com.example.randomtodaylaunch.model.FoodEntity
import com.example.randomtodaylaunch.ui.DetailActivity


/* 음식점 리스트 어댑터 */
class ListAdapter(private val food: List<FoodEntity>, private val context : Context) :
    RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val item = food[position]
        holder.name.text = item.name
        holder.type.text = item.type

        holder.container.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("fname", item.name)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = food.size

    inner class ListViewHolder(binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        val name = binding.itemName
        val type = binding.itemType
        val container = binding.itemContainer
    }
}