package com.example.randomtodaylaunch.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomtodaylaunch.data.DatabaseCopier
import com.example.randomtodaylaunch.data.FoodDataBase
import com.example.randomtodaylaunch.data.ListDAO
import com.example.randomtodaylaunch.model.FoodEntity
import kotlinx.coroutines.launch

class ListViewModel : ViewModel() {

    private val listDAO : ListDAO = DatabaseCopier.INSTANCE!!.listDAO()

    val allFood : LiveData<List<FoodEntity>> by lazy {
        listDAO.getAllFood()
    }

    private val _typeFood = MutableLiveData<List<FoodEntity>>()
    val typeFood : LiveData<List<FoodEntity>> get() = _typeFood

    fun getTypeFood(type : String) {
        viewModelScope.launch {
            val tmp : List<FoodEntity> = listDAO.getTypeFood(type)
            _typeFood.postValue(tmp)
        }
    }
}