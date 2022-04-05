package com.hoya.randomtodaylaunch.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
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
class ListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding
    private lateinit var viewModel: ListViewModel
    private lateinit var adapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModelFactory = ListViewModel.ListViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory).get(ListViewModel::class.java)

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

//        viewModel.getFoodList().observe(this) {
//            adapter.submitList(it)
//        }


        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            // 클릭 뿐 아니라 스와이프로도 이벤트 발생
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val query = SimpleSQLiteQuery("SELECT * FROM food WHERE type IN ('${tab?.text.toString()}')")
                viewModel.getFoodList(query).observe(this@ListActivity) {
                    adapter.submitList(it)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // NOT IMPLEMENTS
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // NOT IMPLEMENTS
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_result, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.action_mail -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "plain/text"
                val address = arrayOf("ropem_@naver.com")
                intent.putExtra(Intent.EXTRA_EMAIL, address)
                startActivity(intent)
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }




}