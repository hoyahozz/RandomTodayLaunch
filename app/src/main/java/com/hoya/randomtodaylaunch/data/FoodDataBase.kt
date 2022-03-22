package com.hoya.randomtodaylaunch.data

import android.content.pm.PackageInfo
import android.util.Log
import androidx.core.content.pm.PackageInfoCompat
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hoya.randomtodaylaunch.model.FoodEntity
import com.hoya.randomtodaylaunch.model.MenuEntity

@Database(entities = [FoodEntity::class, MenuEntity::class], version = 2)
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
        // 1_2 -> 1버전에서 2버전으로 감 (DB를 외부에서 가져올 경우 코드 자체에서 만든 DB가 1버전, 외부에서 가져온 DB가 2버전이 됨
        val MIGRATION_1_2 : Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 버전 업그레이드 시 컬럼이 추가되거나 기본키가 바뀔 때 선언하는 곳 (옮기기만 하는 것이라 지금은 상관 X)
            }
        }
    }
}