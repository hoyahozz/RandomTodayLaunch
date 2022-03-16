package com.hoya.randomtodaylaunch.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import androidx.sqlite.db.SimpleSQLiteQuery
import com.hoya.randomtodaylaunch.data.DatabaseCopier
import com.hoya.randomtodaylaunch.data.ListDAO
import com.hoya.randomtodaylaunch.model.FoodEntity
import com.hoya.randomtodaylaunch.model.MenuEntity
import kotlinx.coroutines.launch

class ListViewModel : ViewModel() {

    private val listDAO : ListDAO = DatabaseCopier.INSTANCE!!.listDAO()

    val allFood : LiveData<List<FoodEntity>> = listDAO.getAllFood()

    private val _typeFood = MutableLiveData<List<FoodEntity>>()
    val typeFood : LiveData<List<FoodEntity>> get() = _typeFood

    private val _menuList = MutableLiveData<List<MenuEntity>>()
    val menuList : LiveData<List<MenuEntity>> get() = _menuList

    fun getFoodList(query: SimpleSQLiteQuery) { // 음식점 리스트
        viewModelScope.launch {
            val tmp : List<FoodEntity> = listDAO.getFood(query)
            _typeFood.postValue(tmp)
        }
    }

    fun getMenuList(fname : String) { // 메뉴 리스트
        viewModelScope.launch {
            val tmp : List<MenuEntity> = listDAO.getMenuList(fname)
            _menuList.postValue(tmp)
        }
    }

}