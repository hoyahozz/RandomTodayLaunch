package com.example.randomtodaylaunch.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable


// 태스크 테이블
@Entity(
    tableName="food",
//    foreignKeys = arrayOf(ForeignKey // 외래키 지정
//        (entity = CategoryEntity::class,
//        parentColumns = arrayOf("id"),
//        childColumns = arrayOf("cid"),
//        onDelete = ForeignKey.CASCADE)))
//    indices = [Index(value = ["pid"], unique = true)] // 유니크키 설정
)
data class FoodEntity(
    @PrimaryKey(autoGenerate = true)
    var id : Int?,
    var type : String,
    var name : String,
)
