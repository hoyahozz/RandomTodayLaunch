package com.hoya.randomtodaylaunch.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoya.randomtodaylaunch.databinding.ItemMenuListBinding
import com.hoya.randomtodaylaunch.model.MenuEntity

/* 상세 화면의 메뉴 리스트 어댑터  */
class MenuAdapter : RecyclerView.Adapter<MenuAdapter.ListViewHolder>() {

    private val menu = arrayListOf<MenuEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemMenuListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    fun submitList(menu: List<MenuEntity>) {
        this.menu.clear()
        this.menu.addAll(menu)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        holder.onBind(menu[position])
    }

    override fun getItemCount(): Int = menu.size

    inner class ListViewHolder(private val binding: ItemMenuListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: MenuEntity) {
            binding.item = item
        }
    }
}