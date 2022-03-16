package com.hoya.randomtodaylaunch.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "food",
    indices = [Index(value = ["name"], unique = true)] // 유니크키 설정
)
data class FoodEntity(
    @PrimaryKey
    var id: Int?,

    var type: String?,

    var name: String?
) : Serializable // 인텐트를 위한 직렬화
