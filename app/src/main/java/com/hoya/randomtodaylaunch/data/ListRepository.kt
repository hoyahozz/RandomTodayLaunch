package com.hoya.randomtodaylaunch.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.hoya.randomtodaylaunch.data.dao.ListDAO
import com.hoya.randomtodaylaunch.data.entity.FoodEntity
import com.hoya.randomtodaylaunch.data.entity.MenuEntity

class ListRepository (application: Application){

    private val listDatabase = DatabaseCopier.getInstance(application)!!
    private val listDAO : ListDAO = listDatabase.listDAO()

    companion object{
        private var sInstance: ListRepository? = null
        fun getInstance(): ListRepository {
            return sInstance
                ?: synchronized(this){
                    val instance = ListRepository(Application())
                    sInstance = instance
                    instance
                }
        }
    }


    fun allList() : LiveData<List<FoodEntity>> {
        return listDAO.getAllFood()
    }

    fun getFoodList(query: SimpleSQLiteQuery) : LiveData<List<FoodEntity>> { // 음식점 리스트
        return listDAO.getFood(query)
    }

    fun getMenuList(fname : String): LiveData<List<MenuEntity>> { // 메뉴 리스트
        return listDAO.getMenuList(fname)
    }
}