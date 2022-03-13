package com.example.randomtodaylaunch.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.randomtodaylaunch.data.DatabaseCopier
import com.example.randomtodaylaunch.data.FoodDataBase
import com.example.randomtodaylaunch.databinding.ActivityMainBinding
import com.google.android.material.chip.Chip
import kotlinx.coroutines.*

/* 초기 화면 - 메인 액티비티 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: FoodDataBase
    private lateinit var job: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 다크 모드 비활성화 설정
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = DatabaseCopier.getInstance(context = applicationContext)!!

        job = CoroutineScope(Dispatchers.IO).launch {
            DatabaseCopier.copyAttachedDatabase(context = applicationContext)
        }

        runBlocking { // 시간이 오래걸리면 강제 종료
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

        // 선택 버튼 눌렀을 때 행동
        binding.pickBtn.setOnClickListener {
            if (checkList.isEmpty()) {
                Toast.makeText(this, "한 가지는 선택해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(applicationContext, ResultActivity::class.java)
                intent.putStringArrayListExtra("checkList", checkList)
                startActivity(intent)
            }
        }

        // 아무거나 버튼 눌렀을 때 행동
        binding.anyBtn.setOnClickListener {
            val allList = arrayListOf("한식","중식","일식","양식","분식","치킨","술집")
            val intent = Intent(applicationContext, ResultActivity::class.java)
            intent.putStringArrayListExtra("checkList", allList)
            startActivity(intent)
        }

        // 리스트 보기 버튼 눌렀을 때 행동
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