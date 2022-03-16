package com.hoya.randomtodaylaunch.data

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.hoya.randomtodaylaunch.model.FoodEntity
import com.hoya.randomtodaylaunch.model.MenuEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ListDAO {

    @Query("SELECT * FROM food")
    fun getAllFood() : LiveData<List<FoodEntity>>

    @Query("SELECT * FROM menu where fname = :fname order by price ASC")
    suspend fun getMenuList(fname : String) : List<MenuEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(foodEntity: FoodEntity)

    @RawQuery
    suspend fun getFood(query : SupportSQLiteQuery) : List<FoodEntity>
}