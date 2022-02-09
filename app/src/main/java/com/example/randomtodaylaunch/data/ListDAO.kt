package com.example.randomtodaylaunch.data

import androidx.room.*
import com.example.randomtodaylaunch.model.FoodEntity

/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-10-24
 * @Description : Data Access Object 를 정의하는 인터페이스
 */


@Dao
interface ListDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(foodEntity: FoodEntity)

    @Query("SELECT * FROM food where type = :type")
    fun getAllFood(type : String) : List<FoodEntity>
}