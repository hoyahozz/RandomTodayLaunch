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
import android.view.View
import android.widget.RadioButton
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.randomtodaylaunch.data.DatabaseCopier
import com.example.randomtodaylaunch.data.FoodDataBase
import com.example.randomtodaylaunch.databinding.ActivityMainBinding
import com.example.randomtodaylaunch.model.FoodEntity
import kotlinx.coroutines.*
import java.util.*
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: FoodDataBase
    private var foodType = "한식"
    private lateinit var getFoodList: List<FoodEntity>
    private lateinit var job: Job
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = DatabaseCopier.getInstance(context = applicationContext)!!

        job = CoroutineScope(Dispatchers.IO).launch {
            DatabaseCopier.copyAttachedDatabase(context = applicationContext)
        }

        runBlocking {
            job.join()
        }


        binding.chipGroup.layoutDirection = View.LAYOUT_DIRECTION_LOCALE
        binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->
            Log.d(TAG, "chipGroup ON")
            when(checkedId) {
                R.id.chip_korea -> {
                    foodType = "한식"
                }
                R.id.chip_china -> {
                    foodType = "중식"
                }

                R.id.chip_japan -> {
                    foodType = "일식"
                }

                R.id.chip_europe -> {
                    foodType = "양식"
                }

                R.id.chip_drink -> {
                    foodType = "술집"
                }

                R.id.chip_snack -> {
                    foodType = "분식"
                }
            }

        }



//        GlobalScope.launch {
//            insertDefaultFoodList()
//        }

        binding.pick.setOnClickListener {
            GlobalScope.launch {
                Log.d(TAG, "onCreate: $foodType")
//                val query = SimpleSQLiteQuery("SELECT * FROM food where type = $foodType")
                getFoodList = db.listDAO().getAllFood(foodType)

                for (element in getFoodList) {
                    Log.d("MainActivity", "onCreate: ${element.name}")
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

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}