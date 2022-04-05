package com.hoya.randomtodaylaunch.viewModel

import androidx.lifecycle.*
import androidx.sqlite.db.SimpleSQLiteQuery
import com.hoya.randomtodaylaunch.data.ListRepository
import com.hoya.randomtodaylaunch.data.entity.FoodEntity
import com.hoya.randomtodaylaunch.data.entity.MenuEntity

class ListViewModel(private val listRepository: ListRepository) : ViewModel() {

    val allFood : LiveData<List<FoodEntity>> = listRepository.allList()

    fun getFoodList(query : SimpleSQLiteQuery): LiveData<List<FoodEntity>> {
        return listRepository.getFoodList(query)
    }

    fun getMenuList(fname : String): LiveData<List<MenuEntity>> {
        return listRepository.getMenuList(fname)
    }

    class ListViewModelFactory : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ListViewModel(ListRepository.getInstance()) as T
        }
    }


}