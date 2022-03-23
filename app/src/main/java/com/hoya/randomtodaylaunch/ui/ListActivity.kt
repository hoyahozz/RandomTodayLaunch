package com.hoya.randomtodaylaunch.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.marginTop
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmadhamwi.tabsync.TabbedListMediator
import com.hoya.randomtodaylaunch.adapter.ListAdapter
import com.hoya.randomtodaylaunch.util.RecyclerViewDecoration
import com.hoya.randomtodaylaunch.databinding.ActivityListBinding
import com.hoya.randomtodaylaunch.model.FoodEntity
import com.hoya.randomtodaylaunch.viewModel.ListViewModel


/* 음식점 리스트 액티비티 */
/* TODO :: 타입 별로 음식 리스트 볼 수 있게 설정하기
*  TODO :: 뷰페이저를 활용해서 메뉴별로 볼 수 있게
* */
class ListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding
    private val viewModel: ListViewModel by viewModels()
    private lateinit var adapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ListAdapter(this)

        binding.listRcv.apply {
            this.adapter = this@ListActivity.adapter
            this.layoutManager = LinearLayoutManager(this@ListActivity)
            this.addItemDecoration(RecyclerViewDecoration(15))
        }


        viewModel.allFood.observe(this) { list ->
            Log.d("ListAct", "$list")

            // 탭 래이아웃 아이템 추가
            for (i in list.distinctBy { it.type }) {
                binding.tabLayout.addTab(binding.tabLayout.newTab().setText(i.type))
            }

            adapter.submitList(list)
        }
    }


}