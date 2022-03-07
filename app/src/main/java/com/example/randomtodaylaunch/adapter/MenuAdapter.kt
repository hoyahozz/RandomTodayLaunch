package com.example.randomtodaylaunch.adapter

import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.randomtodaylaunch.databinding.ItemMenuListBinding
import com.example.randomtodaylaunch.model.MenuEntity

/* 상세 화면의 메뉴 리스트 어댑터  */
class MenuAdapter() : RecyclerView.Adapter<MenuAdapter.ListViewHolder>(){

    private val menu = arrayListOf<MenuEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemMenuListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    fun submitList(menu : List<MenuEntity>) {
        this.menu.clear()
        this.menu.addAll(menu)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = menu[position]

        if(item.id == 999999) {
            Log.d("Menu Adapter", "NO MENU")
            holder.name.visibility = View.GONE
            holder.price.visibility = View.GONE
            holder.empty.text = "메뉴가 등록되어 있지 않아요 😭"
            holder.empty.visibility = View.VISIBLE
        } else {
            holder.name.text = item.name
            holder.price.text = item.price.toString()
        }
    }

    override fun getItemCount(): Int = menu.size

    inner class ListViewHolder(binding : ItemMenuListBinding) : RecyclerView.ViewHolder(binding.root) {
        val name = binding.itemName
        val price = binding.itemPrice
        val empty = binding.itemEmpty
    }
}