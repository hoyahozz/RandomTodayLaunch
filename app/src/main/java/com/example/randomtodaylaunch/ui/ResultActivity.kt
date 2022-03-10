package com.example.randomtodaylaunch.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        val getCheckList = intent.getStringArrayListExtra("checkList")!! // 메인에서 선택한 음식 종류
        lateinit var randomList: ArrayList<FoodEntity>
        var randomInt: Int
        lateinit var result: FoodEntity

        // 애니메이션 설정
        val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.shake)
        binding.ivRotate.startAnimation(animation)

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
                uiVisibleControl(View.INVISIBLE)
            }

            override fun onAnimationEnd(p0: Animation?) {
                uiVisibleControl(View.VISIBLE)
            }

            override fun onAnimationRepeat(p0: Animation?) {}
        })

        // 종류에 따라 메뉴를 갱신하기 위한 쿼리문
        val query = SimpleSQLiteQuery("SELECT * FROM food WHERE type IN ('${getCheckList.joinToString("','")}')")

        viewModel.getFoodList(query) // 뷰모델에서 쿼리문 실행

        // 종류 갱신 후 행동
        viewModel.typeFood.observe(this) {
            randomList = it as ArrayList<FoodEntity> // 랜덤 리스트 초기화

            randomInt = Random().nextInt(it.size - 1) // 랜덤 숫자 출력
            result = it[randomInt]

            binding.food = result // DataBinding
            result.name?.let {
                viewModel.getMenuList(result.name!!) } // 종류에 따른 메뉴리스트 갱신
        }

        val adapter = MenuAdapter()
        binding.rcvMenu.addItemDecoration(RecyclerViewDecoration(10))
        binding.rcvMenu.adapter = adapter
        binding.rcvMenu.layoutManager = LinearLayoutManager(this)

        // 종류 다시 고르기 버튼을 눌렀을 때
        binding.btnRestart.setOnClickListener {
            finish()
        }

        // 메뉴 리스트 옵저빙 후 조치
        viewModel.menuList.observe(this) {
            adapter.submitList(it)

            if (it.isEmpty()) { // 메뉴 리스트가 없으면 안내
                binding.itemEmpty.text = "메뉴가 등록되어 있지 않아요 😭"
            } else {
                binding.itemEmpty.text = ""
            }
        }

        // 자세한 정보 확인하기
        binding.btnInfo.setOnClickListener {

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
        }
    }

    // UI 일시 수정
    fun uiVisibleControl(visibility : Int) {
        binding.itemEmpty.visibility = visibility
        binding.rcvMenu.visibility = visibility
        binding.tvTitle.visibility = visibility
        binding.layoutText.visibility = visibility
        binding.btnRestart.visibility = visibility
        binding.btnInfo.visibility = visibility
        binding.btnRedecide.visibility = visibility
    }

}