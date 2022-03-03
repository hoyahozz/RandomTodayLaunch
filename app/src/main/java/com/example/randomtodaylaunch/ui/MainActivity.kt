package com.example.randomtodaylaunch.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.randomtodaylaunch.data.DatabaseCopier
import com.example.randomtodaylaunch.data.FoodDataBase
import com.example.randomtodaylaunch.databinding.ActivityMainBinding
import com.example.randomtodaylaunch.model.FoodEntity
import com.example.randomtodaylaunch.viewModel.ListViewModel
import com.google.android.material.chip.Chip
import kotlinx.coroutines.*
import java.util.*

/* 초기 화면 - 메인 액티비티 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: FoodDataBase
    private lateinit var getFoodList: List<FoodEntity>
    private lateinit var job: Job
    private val TAG = "MainActivity"
    private val viewModel : ListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        val checkList = mutableListOf<String>() // 선택한 리스트를 담는 공간

        // 다중 칩그룹 선택에 따른 데이터 추가
        for (index in 0 until binding.chipGroup.childCount) {
            val chip : Chip = binding.chipGroup.getChildAt(index) as Chip

            chip.setOnCheckedChangeListener { view, isChecked ->
                if(isChecked) {
                    checkList.add(view.text.toString())
                    val query = SimpleSQLiteQuery("SELECT * FROM food WHERE type IN ('${checkList.joinToString("','")}')")
                    viewModel.getFoodList(query)
                } else {
                    checkList.remove(view.text.toString())
                    val query = SimpleSQLiteQuery("SELECT * FROM food WHERE type IN ('${checkList.joinToString("','")}')")
                    viewModel.getFoodList(query)
                }

                if (checkList.isNotEmpty()) {
                    Log.d(TAG, "'SELECT * FROM food WHERE type IN ('${checkList.joinToString("','")}')")
                }
            }
        }

        
        viewModel.typeFood.observe(this) {
            getFoodList = it
        }

        binding.pickBtn.setOnClickListener {
            if(checkList.isEmpty()) {
                Toast.makeText(this, "한 가지는 선택해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                val random = Random()
                if(getFoodList.isNotEmpty()) {
                    val randomInt = random.nextInt(getFoodList.size - 1)

                    val result = getFoodList[randomInt]

                    val intent = Intent(applicationContext, ResultActivity::class.java)
                    intent.putExtra("result", result)

                    startActivity(intent)
                } else {
                    Toast.makeText(this, "데이터가 존재하지 않아요.\n다른 종류를 선택해주세요.", Toast.LENGTH_SHORT).show()
                }
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