package com.example.randomtodaylaunch.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.randomtodaylaunch.R
import com.example.randomtodaylaunch.adapter.MenuAdapter
import com.example.randomtodaylaunch.databinding.ActivityResultBinding
import com.example.randomtodaylaunch.model.FoodEntity
import com.example.randomtodaylaunch.util.RecyclerViewDecoration
import com.example.randomtodaylaunch.viewModel.ListViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar.ANIMATION_MODE_SLIDE
import com.google.android.material.snackbar.Snackbar
import java.util.*


/* 랜덤 결과 출력 액티비티 */
class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private val viewModel: ListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back)

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
        val query =
            SimpleSQLiteQuery("SELECT * FROM food WHERE type IN ('${getCheckList.joinToString("','")}')")

        viewModel.getFoodList(query) // 뷰모델에서 쿼리문 실행

        // 종류 갱신 후 행동
        viewModel.typeFood.observe(this) {
            randomList = it as ArrayList<FoodEntity> // 랜덤 리스트 초기화

            randomInt = Random().nextInt(it.size - 1) // 랜덤 숫자 출력
            result = it[randomInt]

            binding.food = result // DataBinding
            result.name?.let {
                viewModel.getMenuList(result.name!!)
            } // 종류에 따른 메뉴리스트 갱신
        }

        val adapter = MenuAdapter()
        binding.rcvMenu.addItemDecoration(RecyclerViewDecoration(10))
        binding.rcvMenu.adapter = adapter
        binding.rcvMenu.layoutManager = LinearLayoutManager(this)

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
            val uri = "고척 ${result.name}" // 고척 {음식점이름}

            val intent =
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://m.map.naver.com/search2/search.naver?query=${uri}&sm=hty&style=v5#/list")
                )
            startActivity(intent)
        }

        var isEmptyRandomList: Boolean = false // 랜덤 리스트에 하나밖에 존재하지 않을 때 true

        // 빼고 다시돌리기
        binding.btnRedecide.setOnClickListener {
            randomList.remove(result)

            if (randomList.size - 1 == 0) { // 리스트에 하나밖에 존재하지 않을 경우
                if (isEmptyRandomList) {
                    val snackBar = Snackbar.make(it, "더 이상 종류가 없어요!", Snackbar.LENGTH_SHORT)
                    setSnackBarOption(snackBar)
                    snackBar.show()

                    return@setOnClickListener
                }

                // 리스트에 하나밖에 없을 때, 일단 남은 리스트를 유저에게 제공해준다.
                binding.ivRotate.startAnimation(animation)
                binding.food = randomList[0]
                viewModel.getMenuList(randomList[0].name!!)
                isEmptyRandomList = true // 리스트가 남아있지 않으니 랜덤 리스트 추가 제공 X

            } else { // 애니메이션과 함께 다시 데이터 분석
                binding.ivRotate.startAnimation(animation)

                randomInt = Random().nextInt(randomList.size - 1)
                result = randomList[randomInt]
                binding.food = result // Data Binding
                result.name?.let { viewModel.getMenuList(it) }
            }
        }
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

    // UI 일시 수정
    private fun uiVisibleControl(visibility: Int) {
        binding.itemEmpty.visibility = visibility
        binding.rcvMenu.visibility = visibility
        binding.tvTitle.visibility = visibility
        binding.layoutText.visibility = visibility
        binding.btnInfo.visibility = visibility
        binding.btnRedecide.visibility = visibility
    }

    // 스낵바 옵션 설정
    private fun setSnackBarOption(snackBar: Snackbar) {
        snackBar.animationMode = ANIMATION_MODE_SLIDE
        val snackBarView = snackBar.view
        val snackBarText = snackBarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        val snackBarLayout : FrameLayout.LayoutParams = snackBarView.layoutParams as FrameLayout.LayoutParams
        snackBarLayout.gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
        snackBarLayout.width = 800
        snackBarLayout.height = 130
        snackBarText.textAlignment = View.TEXT_ALIGNMENT_CENTER
        snackBarText.typeface = Typeface.createFromAsset(this.assets, "context.ttf")
        snackBarView.setBackgroundColor(Color.parseColor("#4354F1"))
    }

}