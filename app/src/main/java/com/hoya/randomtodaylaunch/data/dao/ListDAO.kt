package com.hoya.randomtodaylaunch.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.hoya.randomtodaylaunch.data.entity.FoodEntity
import com.hoya.randomtodaylaunch.data.entity.MenuEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ListDAO {

    @Query("SELECT * FROM food")
    fun getAllFood() : LiveData<List<FoodEntity>>

    @Query("SELECT * FROM menu where fname = :fname order by price ASC")
    fun getMenuList(fname : String) : LiveData<List<MenuEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(foodEntity: FoodEntity)

    @RawQuery(observedEntities = [FoodEntity::class])
    fun getFood(query : SupportSQLiteQuery) : LiveData<List<FoodEntity>>
}