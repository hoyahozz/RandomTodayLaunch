package com.example.randomtodaylaunch.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.randomtodaylaunch.databinding.ItemMenuListBinding
import com.example.randomtodaylaunch.model.MenuEntity

class MenuAdapter(private val menu : List<MenuEntity>) : RecyclerView.Adapter<MenuAdapter.ListViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemMenuListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = menu[position]

        holder.name.text = item.name
        holder.price.text = item.price.toString()
    }

    override fun getItemCount(): Int = menu.size

    inner class ListViewHolder(binding : ItemMenuListBinding) : RecyclerView.ViewHolder(binding.root) {
        val name = binding.itemName
        val price = binding.itemPrice
    }
}