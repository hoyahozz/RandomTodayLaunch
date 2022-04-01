package com.hoya.randomtodaylaunch.ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.pm.PackageInfoCompat
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.hoya.randomtodaylaunch.data.DatabaseCopier
import com.hoya.randomtodaylaunch.data.FoodDataBase
import com.hoya.randomtodaylaunch.databinding.ActivityMainBinding
import kotlinx.coroutines.*


/* 초기 화면 - 메인 액티비티 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: FoodDataBase
    private var job: Job? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 저장되어 있는 버전 체크
        val prefs = getSharedPreferences("MyVersion", MODE_PRIVATE)
        val editor = prefs.edit()
        val last = prefs.getLong("LAST_VERSION", 0)
        // 현재 앱 버전 체크
        val info: PackageInfo = applicationContext.packageManager.getPackageInfo(applicationContext.packageName, 0)
        val version = PackageInfoCompat.getLongVersionCode(info)

        // 다크 모드 비활성화 설정
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DatabaseCopier.getInstance(context = applicationContext)!!

        // 버전이 업데이트 됐다고 판단하면 데이터베이스도 함께 업데이트한다.
        if (last < version) {
            editor.putLong("LAST_VERSION", version)
            editor.apply()

            job = CoroutineScope(Dispatchers.IO).launch {
                DatabaseCopier.copyAttachedDatabase(context = applicationContext)
            }
        }

        runBlocking { // 시간이 오래걸리면 강제 종료
            job?.join()
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
                val snackBar = Snackbar.make(it, "한 가지 종류는 골라주세요!", Snackbar.LENGTH_SHORT)
                setSnackBarOption(snackBar)
                snackBar.show()
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

        // TODO :: 리스트 보기 버튼 눌렀을 때 행동
        binding.listBtn.setOnClickListener {
            val intent = Intent(applicationContext, ListActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

    // 스낵바 옵션 설정
    private fun setSnackBarOption(snackBar: Snackbar) {
        snackBar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
        val snackBarView = snackBar.view
        val snackBarText =
            snackBarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        val snackBarLayout: FrameLayout.LayoutParams =
            snackBarView.layoutParams as FrameLayout.LayoutParams
        snackBarLayout.gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
        snackBarLayout.width = 650
        snackBarLayout.height = 120
        snackBarText.textAlignment = View.TEXT_ALIGNMENT_CENTER
        snackBarText.typeface = Typeface.createFromAsset(this.assets, "context.ttf")
        snackBarView.setBackgroundColor(Color.parseColor("#4354F1"))
    }
}