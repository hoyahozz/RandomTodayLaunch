package com.example.randomtodaylaunch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.randomtodaylaunch.databinding.ActivityMainBinding
import com.example.randomtodaylaunch.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var binding : ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val getResult = intent.getStringExtra("result")

        binding.result.text = getResult
    }
}