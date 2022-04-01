package com.hoya.randomtodaylaunch.data

import android.content.Context
import android.content.pm.PackageInfo
import android.util.Log
import androidx.core.content.pm.PackageInfoCompat
import androidx.room.Room
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/* 데이터베이스 카피 및 생성 */
object DatabaseCopier {

    private const val TAG = "DatabaseCopier"
    private const val DB_NAME = "defaultDatabase.db"
    var INSTANCE : FoodDataBase? = null

    fun getInstance(context: Context): FoodDataBase? {
        if (INSTANCE == null) {
            Log.d(TAG, "INSTANCE NULL")
            synchronized(FoodDataBase::class) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    FoodDataBase::class.java, DB_NAME
                )
                    .addMigrations(FoodDataBase.MIGRATION_1_2)
                    .build()
            }
        } else {
            Log.d(TAG, "getInstance: INSTANCE NOT NULL")
        }
        return INSTANCE
    }

    // assets -> App / Database
    fun copyAttachedDatabase(context: Context) {
        val dbPath = context.getDatabasePath(DB_NAME)

        // db 파일이 있을 때
        if (dbPath.exists()) {
            context.deleteDatabase(DB_NAME) // 기존 디비 파일 삭제
            copyDB(context, dbPath)
            return
        }

        // 폴더 만들어주기
        dbPath.parentFile.mkdirs()
        copyDB(context, dbPath)
    }


    private fun copyDB(context: Context, _dbPath : File) {
        try {

            Log.w(TAG, "copyDB")

            val inputStream = context.assets.open("databases/$DB_NAME")
            val output = FileOutputStream(_dbPath)

            val buffer = ByteArray(8192)
            var length : Int

            while(true) {
                length = inputStream.read(buffer, 0, 8192)
                if(length <= 0) break
                output.write(buffer, 0, length)
            }

            output.flush()
            output.close()
            inputStream.close()
        } catch (e : IOException) {
            Log.e(TAG, "copyDB: 실패!", e)
            e.printStackTrace()
        }
    }

}