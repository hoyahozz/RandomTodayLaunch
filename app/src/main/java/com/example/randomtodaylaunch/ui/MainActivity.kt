package com.example.randomtodaylaunch.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.randomtodaylaunch.data.DatabaseCopier
import com.example.randomtodaylaunch.data.FoodDataBase
import com.example.randomtodaylaunch.databinding.ActivityMainBinding
import com.example.randomtodaylaunch.model.FoodEntity
import com.example.randomtodaylaunch.viewModel.ListViewModel
import com.google.android.material.chip.Chip
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

/* 초기 화면 - 메인 액티비티 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: FoodDataBase
    private lateinit var getFoodList: List<FoodEntity>
    private lateinit var job: Job
    private val TAG = "MainActivity"
    private val viewModel: ListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = DatabaseCopier.getInstance(context = applicationContext)!!

        job = CoroutineScope(Dispatchers.IO).launch {
            DatabaseCopier.copyAttachedDatabase(context = applicationContext)
        }

        runBlocking {
            job.join()
        }

        binding.chipGroup.layoutDirection = View.LAYOUT_DIRECTION_LOCALE

        val checkList = arrayListOf<String>() // 선택한 리스트를 담는 공간

        // 다중 칩그룹 선택에 따른 데이터 추가
        for (index in 0 until binding.chipGroup.childCount) {
            val chip: Chip = binding.chipGroup.getChildAt(index) as Chip

            chip.setOnCheckedChangeListener { view, isChecked ->
                if (isChecked) {
                    checkList.add(view.text.toString())
                } else {
                    checkList.remove(view.text.toString())
                }
            }
        }

        viewModel.typeFood.observe(this) {
            getFoodList = it
        }

        binding.pickBtn.setOnClickListener {
            if (checkList.isEmpty()) {
                Toast.makeText(this, "한 가지는 선택해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(applicationContext, ResultActivity::class.java)
                intent.putStringArrayListExtra("checkList", checkList)
                startActivity(intent)
            }
        }

        binding.listBtn.setOnClickListener {
            val intent = Intent(applicationContext, ListActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}