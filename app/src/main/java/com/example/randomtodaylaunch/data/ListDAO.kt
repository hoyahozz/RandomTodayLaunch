package com.example.randomtodaylaunch.data

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.randomtodaylaunch.model.FoodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ListDAO {

    @Query("SELECT * FROM food")
    fun getAllFood() : LiveData<List<FoodEntity>>

    @Query("SELECT * FROM food where type = :type")
    suspend fun getTypeFood(type : String) : List<FoodEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(foodEntity: FoodEntity)

    @RawQuery
    suspend fun getFood(query : SupportSQLiteQuery) : List<FoodEntity>
}