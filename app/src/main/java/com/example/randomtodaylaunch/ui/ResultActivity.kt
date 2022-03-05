package com.example.randomtodaylaunch.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.randomtodaylaunch.R
import com.example.randomtodaylaunch.adapter.MenuAdapter
import com.example.randomtodaylaunch.adapter.RecyclerViewDecoration
import com.example.randomtodaylaunch.databinding.ActivityResultBinding
import com.example.randomtodaylaunch.model.FoodEntity
import com.example.randomtodaylaunch.model.MenuEntity
import com.example.randomtodaylaunch.viewModel.ListViewModel
import java.util.*
import kotlin.collections.ArrayList

/* 랜덤 결과 출력 액티비티 */
class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private val viewModel: ListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val getCheckList = intent.getStringArrayListExtra("checkList")!!
        lateinit var randomList: ArrayList<FoodEntity>
        var randomInt: Int
        lateinit var result: FoodEntity

        val query =
            SimpleSQLiteQuery("SELECT * FROM food WHERE type IN ('${getCheckList.joinToString("','")}')")
        viewModel.getFoodList(query)

        viewModel.typeFood.observe(this) {

            randomList = it as ArrayList<FoodEntity>

            randomInt = Random().nextInt(it.size - 1)
            result = it[randomInt]
            binding.food = result

            result.name?.let { viewModel.getMenuList(result.name!!) }
        }

        binding.btnRestart.setOnClickListener {
            finish()
        }

        val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.rotate)
        animation.duration = 1000
        binding.ivRotate.startAnimation(animation)

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
                binding.result.visibility = View.GONE
                binding.btnRestart.visibility = View.GONE
                binding.btnWebView.visibility = View.GONE
                binding.btnRedecide.visibility = View.GONE
            }

            override fun onAnimationEnd(p0: Animation?) {
                binding.result.visibility = View.VISIBLE
                binding.btnRestart.visibility = View.VISIBLE
                binding.btnWebView.visibility = View.VISIBLE
                binding.btnRedecide.visibility = View.VISIBLE
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }
        })

        binding.rcvMenu.addItemDecoration(RecyclerViewDecoration(10))

        viewModel.menuList.observe(this) {
            binding.rcvMenu.apply {

                if (it.isEmpty()) {
                    val noMenu = listOf(MenuEntity(999999, null, null, null, null))
                    this.adapter = MenuAdapter(noMenu)
                    this.layoutManager = LinearLayoutManager(context)
                } else {
                    this.adapter = MenuAdapter(it)
                    this.layoutManager = LinearLayoutManager(context)
                }
            }
        }

        // 자세한 정보 확인하기
        binding.btnWebView.setOnClickListener {

            val uri = "고척 ${result.name}"

            val intent =
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://m.map.naver.com/search2/search.naver?query=${uri}&sm=hty&style=v5#/list")
                )

            startActivity(intent)
        }

        // 빼고 다시돌리기
        binding.btnRedecide.setOnClickListener {
            randomList.remove(result)

            if (randomList.size - 1 == 0) {
                Toast.makeText(this, "더 이상 종류가 없어요 ㅠㅠ", Toast.LENGTH_SHORT).show()
            } else {
                binding.ivRotate.startAnimation(animation)
                randomInt = Random().nextInt(randomList.size - 1)
                result = randomList[randomInt]

                binding.food = result
                result.name?.let { viewModel.getMenuList(result.name!!) }
            }
            Log.d("ResultActivity", "$randomList")
        }


    }
}