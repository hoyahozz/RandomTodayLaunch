package com.example.randomtodaylaunch.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.randomtodaylaunch.data.DatabaseCopier
import com.example.randomtodaylaunch.data.ListDAO
import com.example.randomtodaylaunch.model.FoodEntity
import com.example.randomtodaylaunch.model.MenuEntity
import kotlinx.coroutines.launch

class ListViewModel : ViewModel() {

    private val listDAO : ListDAO = DatabaseCopier.INSTANCE!!.listDAO()

    val allFood : LiveData<List<FoodEntity>> by lazy {
        listDAO.getAllFood()
    }

    private val _typeFood = MutableLiveData<List<FoodEntity>>()
    val typeFood : LiveData<List<FoodEntity>> get() = _typeFood

    private val _menuList = MutableLiveData<List<MenuEntity>>()
    val menuList : LiveData<List<MenuEntity>> get() = _menuList


    fun getMenuList(fname : String) {
        viewModelScope.launch {
            val tmp : List<MenuEntity> = listDAO.getMenuList(fname)
            _menuList.postValue(tmp)
        }
    }

    fun getFood(query: SimpleSQLiteQuery) {
        viewModelScope.launch {
            val tmp : List<FoodEntity> = listDAO.getFood(query)
            _typeFood.postValue(tmp)
        }
    }

}