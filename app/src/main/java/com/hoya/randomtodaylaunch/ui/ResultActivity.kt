package com.hoya.randomtodaylaunch.ui

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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.sqlite.db.SimpleSQLiteQuery
import com.hoya.randomtodaylaunch.R
import com.hoya.randomtodaylaunch.adapter.MenuAdapter
import com.hoya.randomtodaylaunch.databinding.ActivityResultBinding
import com.hoya.randomtodaylaunch.data.entity.FoodEntity
import com.hoya.randomtodaylaunch.util.RecyclerViewDecoration
import com.hoya.randomtodaylaunch.viewModel.ListViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar.ANIMATION_MODE_SLIDE
import com.google.android.material.snackbar.Snackbar
import java.util.*


/* 랜덤 결과 출력 액티비티 */
class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private lateinit var viewModel: ListViewModel
    private val adapter: MenuAdapter by lazy {
        MenuAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModelFactory = ListViewModel.ListViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory).get(ListViewModel::class.java)

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


        // 종류 갱신 후 행동
        viewModel.getFoodList(query).observe(this) { it ->
            randomList = it as ArrayList<FoodEntity> // 랜덤 리스트 초기화

            randomInt = Random().nextInt(it.size - 1) // 랜덤 숫자 출력
            result = it[randomInt]

            binding.food = result // DataBinding
            result.name?.let {
                // 메뉴 리스트 옵저빙 후 조치
                viewModel.getMenuList(result.name!!).observe(this) { list ->
                    adapter.submitList(list)

                    if (list.isEmpty()) { // 메뉴 리스트가 없으면 안내
                        binding.itemEmpty.text = "메뉴가 등록되어 있지 않아요 😭"
                    } else {
                        binding.itemEmpty.text = ""
                    }
                }
            } // 종류에 따른 메뉴리스트 갱신
        }


        binding.rcvMenu.addItemDecoration(RecyclerViewDecoration(10))
        binding.rcvMenu.adapter = adapter
        binding.rcvMenu.layoutManager = LinearLayoutManager(this)


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
        binding.btnRedecide.setOnClickListener { list ->

            randomList.remove(result)

            if (randomList.size == 0) { // 더 이상 종류가 없을 경우

                val snackBar = Snackbar.make(list, "더 이상 종류가 없어요!", Snackbar.LENGTH_SHORT)
                setSnackBarOption(snackBar)
                snackBar.show()

                return@setOnClickListener
            } else { // 애니메이션과 함께 다시 데이터 분석

                binding.ivRotate.startAnimation(animation)

                randomInt = Random().nextInt(randomList.size)
                result = randomList[randomInt]
                binding.food = result // Data Binding
                result.name.let { viewModel.getMenuList(it!!) }
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
        val snackBarText =
            snackBarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        val snackBarLayout: FrameLayout.LayoutParams =
            snackBarView.layoutParams as FrameLayout.LayoutParams
        snackBarLayout.gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
        snackBarLayout.bottomMargin = 10
        snackBarLayout.width = 650
        snackBarLayout.height = 120
        snackBarText.textAlignment = View.TEXT_ALIGNMENT_CENTER
        snackBarText.typeface = Typeface.createFromAsset(this.assets, "context.ttf")
        snackBarView.setBackgroundColor(Color.parseColor("#4354F1"))
    }
}