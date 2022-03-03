package com.example.randomtodaylaunch.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.randomtodaylaunch.R
import com.example.randomtodaylaunch.adapter.MenuAdapter
import com.example.randomtodaylaunch.adapter.RecyclerViewDecoration
import com.example.randomtodaylaunch.databinding.ActivityResultBinding
import com.example.randomtodaylaunch.model.FoodEntity
import com.example.randomtodaylaunch.model.MenuEntity
import com.example.randomtodaylaunch.viewModel.ListViewModel

/* 랜덤 결과 출력 액티비티 */
class ResultActivity : AppCompatActivity() {

    private lateinit var binding : ActivityResultBinding
    private val viewModel : ListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val getResult = intent.getSerializableExtra("result") as FoodEntity

        binding.food = getResult

        binding.btnRestart.setOnClickListener {
            finish()
        }

        val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.rotate)
        animation.duration = 1000
        binding.ivRotate.startAnimation(animation)

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                binding.result.visibility = View.VISIBLE
                binding.btnRestart.visibility = View.VISIBLE
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }
        })

        getResult.name?.let { viewModel.getMenuList(it) }

        viewModel.menuList.observe(this) {
            binding.rcvMenu.apply {

                if (it.isEmpty()) {
                    val noMenu = listOf<MenuEntity>(MenuEntity(999999,null,null, null, null))
                    this.adapter = MenuAdapter(noMenu)
                    this.layoutManager = LinearLayoutManager(context)
                } else {
                    this.adapter = MenuAdapter(it)
                    this.layoutManager = LinearLayoutManager(context)
                    this.addItemDecoration(RecyclerViewDecoration(10))
                }

            }




        }


    }
}