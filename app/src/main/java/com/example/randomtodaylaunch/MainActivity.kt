package com.example.randomtodaylaunch

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.example.randomtodaylaunch.data.FoodDataBase
import com.example.randomtodaylaunch.databinding.ActivityMainBinding
import com.example.randomtodaylaunch.model.FoodEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: FoodDataBase
    private var foodType = "한식"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = FoodDataBase.getInstance(this)!! // NOT NULL

        binding.foodRg.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.foodChinaRb -> foodType = "중식"
                R.id.foodKoreanRb -> foodType = "한식"
                R.id.foodJapanRb -> foodType = "일식"
            }
        }

        binding.foodRg2.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.foodDrinkRb -> foodType = "술집"
            }
        }



        GlobalScope.launch {
            insertDefaultFoodList()
        }

        binding.pick.setOnClickListener {

            GlobalScope.launch {
                val getFoodList: List<FoodEntity> = db.listDAO().getAllFood(foodType)

                for(i : Int in 0 until getFoodList.size) {
                    Log.d("MainActivity", "onCreate: ${getFoodList[i].name}")
                }

                val random = Random()
                val randomInt = random.nextInt(getFoodList.size - 1)

                val result = getFoodList[randomInt].name

                val intent = Intent(applicationContext, ResultActivity::class.java)
                intent.putExtra("result", result)
                startActivity(intent)
            }
        }
    }

    // 기본 푸드 리스트 추가
    private suspend fun insertDefaultFoodList() {
        val defaultFoodList: List<FoodEntity> = listOf(
            FoodEntity(null, "한식", "두부이야기"),
            FoodEntity(null, "한식", "국수나무"),
            FoodEntity(null, "한식", "엽기떡볶이"),
            FoodEntity(null, "한식", "청년다방"),
            FoodEntity(null, "한식", "백채 김치찌개"),
            FoodEntity(null, "한식", "구름산추어탕"),
            FoodEntity(null, "한식", "두부이야기"),
            FoodEntity(null, "한식", "놀부부대찌개"),
            FoodEntity(null, "한식", "돈발해"),
            FoodEntity(null, "한식", "한솥도시락"),
            FoodEntity(null, "한식", "전주식당"),
            FoodEntity(null, "한식", "싸움의고수"),
            FoodEntity(null, "한식", "시골집"),
            FoodEntity(null, "한식", "지지고"),
            FoodEntity(null, "일식", "진스시"),
            FoodEntity(null, "일식", "동명"),
            FoodEntity(null, "일식", "최고당돈가스"),
            FoodEntity(null, "일식", "타코비"),
            FoodEntity(null, "일식", "샤브향"),
            FoodEntity(null, "일식", "소담촌"),
            FoodEntity(null, "일식", "고척돈가스"),
            FoodEntity(null, "일식", "난연스시"),
            FoodEntity(null, "중식", "일품양꼬치"),
            FoodEntity(null, "중식", "현양꼬치"),
            FoodEntity(null, "중식", "일품마라탕"),
            FoodEntity(null, "중식", "감성반점"),
            FoodEntity(null, "중식", "하우마라"),
            FoodEntity(null, "중식", "홍콩반점"),
            FoodEntity(null, "술집", "두꺼비로맨스"),
            FoodEntity(null, "술집", "도쿄시장"),
            FoodEntity(null, "술집", "진민네포차"),
            FoodEntity(null, "술집", "76번지"),
            FoodEntity(null, "술집", "금별맥주"),
            FoodEntity(null, "술집", "육회지존"),
            FoodEntity(null, "술집", "303플레이스"),
            FoodEntity(null, "술집", "김떡비"),
            FoodEntity(null, "양식", "언스틸"),
            FoodEntity(null, "양식", "고척동이태리"),
            FoodEntity(null, "양식", "서브웨이")
        )

        for (element in defaultFoodList) {
            db.listDAO().insertFood(element)
        }
    }
}