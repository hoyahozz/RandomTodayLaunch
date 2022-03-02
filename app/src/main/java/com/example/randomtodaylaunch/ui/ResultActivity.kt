package com.example.randomtodaylaunch.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.randomtodaylaunch.R
import com.example.randomtodaylaunch.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var binding : ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val getResult = intent.getStringExtra("result")

        binding.setResult(getResult)

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
    }
}