package com.example.randomtodaylaunch.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.randomtodaylaunch.model.FoodEntity

/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-10-24
 * @Description : Database(Schema) 를 정의하는 클래스
 */

@Database(entities = arrayOf(FoodEntity::class), version = 3)
abstract class FoodDataBase : RoomDatabase() {
    abstract fun listDAO(): ListDAO


/*
    데이터베이스를 만드는 작업은 리소스를 많이 잡아먹기 때문에,
    앱 전체 프로세스 안에서 객체를 한번만 생성하는 것이 유리하다. (Singleton Pattern)

    또한, 개발 중 엔티티(테이블)에 변화가 생기면 우선 버전을 올려주어야 한다.
    과거의 데이터베이스를 옮겨올 것인지, 모든 데이터를 드랍하고 새로운 데이터로 이어갈 것인지
    결정할 수 있다.
 */

    companion object {

        var INSTANCE: FoodDataBase? = null

        fun getInstance(context: Context): FoodDataBase? {
            if (INSTANCE == null) {
                synchronized(FoodDataBase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FoodDataBase::class.java, "food.db"
                    )
                        .addCallback(object : RoomDatabase.Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                            }
                        })
                        .fallbackToDestructiveMigration() // 업데이트시 모든 데이터를 드랍한다.
                        .build()
                }
            }
            return INSTANCE
        }
    }
}