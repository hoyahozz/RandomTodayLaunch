package com.example.randomtodaylaunch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.randomtodaylaunch.R
import com.example.randomtodaylaunch.databinding.ItemListBinding
import com.example.randomtodaylaunch.model.FoodEntity

class ListAdapter(private val food : List<FoodEntity>) : RecyclerView.Adapter<ListAdapter.ListViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val item = food[position]
        holder.name.text = item.name
        holder.type.text = item.type

        if(item.name == "한솥도시락") {
            holder.image.setImageResource(R.drawable.hansol)
        } else {
            holder.image.setImageResource(0)
        }

    }

    override fun getItemCount(): Int = food.size

    inner class ListViewHolder(binding : ItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        val image = binding.itemImage
        val name = binding.itemName
        val type = binding.itemType
    }
}