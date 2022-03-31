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
            synchronized(FoodDataBase::class) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    FoodDataBase::class.java, DB_NAME
                )
                    .addMigrations(FoodDataBase.MIGRATION_1_2)
                    .build()
            }
        } else {
            Log.e(TAG, "getInstance: INSTANCE NOT NULL")
        }
        return INSTANCE
    }

    // assets -> App / Database
    fun copyAttachedDatabase(context: Context) {
        val dbPath = context.getDatabasePath(DB_NAME)


        // db 파일 있으면
        if (dbPath.exists()) {

            val info: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            val version = PackageInfoCompat.getLongVersionCode(info)

            Log.w(TAG, "Version :: $version")

            // 버전 관리 (계속 변경)
            if (version.toString() != "5"){
                Log.w(TAG, "$version :: 버전 코드 다름!")
                copyDB(context, dbPath)
            }
            return
        }

        // 폴더 만들어주기
        dbPath.parentFile.mkdirs()
        copyDB(context, dbPath)
    }


    private fun copyDB(context: Context, _dbPath : File) {
        try {
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