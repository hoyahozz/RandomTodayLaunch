package com.example.randomtodaylaunch.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


/* 리사이클러뷰 간격 조정 */
class RecyclerViewDecoration(private val height: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.bottom = height
    }
}