package com.hoya.randomtodaylaunch.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.sqlite.db.SimpleSQLiteQuery
import com.google.android.material.tabs.TabLayout
import com.hoya.randomtodaylaunch.R
import com.hoya.randomtodaylaunch.adapter.ListAdapter
import com.hoya.randomtodaylaunch.databinding.ActivityListBinding
import com.hoya.randomtodaylaunch.util.RecyclerViewDecoration
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

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back)

        adapter = ListAdapter(this)

        binding.listRcv.apply {
            this.adapter = this@ListActivity.adapter
            this.layoutManager = LinearLayoutManager(this@ListActivity)
            this.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            this.addItemDecoration(RecyclerViewDecoration(50))
        }

        viewModel.allFood.observe(this) { list ->
            // 탭 래이아웃 아이템 추가
            for (i in list.distinctBy { it.type }) {
                binding.tabLayout.addTab(binding.tabLayout.newTab().setText(i.type))
            }
        }

        viewModel.typeFood.observe(this) {
            adapter.submitList(it)
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            // 클릭 뿐 아니라 스와이프로도 이벤트 발생
            override fun onTabSelected(tab: TabLayout.Tab?) {

                Log.d("List", tab?.text.toString())

                val query = SimpleSQLiteQuery("SELECT * FROM food WHERE type IN ('${tab?.text.toString()}')")
                viewModel.getFoodList(query)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // NOT IMPLEMENTS
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // NOT IMPLEMENTS
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }




}