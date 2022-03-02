package com.example.randomtodaylaunch.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "menu",
    foreignKeys = [ForeignKey // 외래키 지정
        (
        entity = FoodEntity::class,
        parentColumns = arrayOf("name"),
        childColumns = arrayOf("fname"),
        onDelete = ForeignKey.CASCADE
    )]
)

data class MenuEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    @NonNull
    var fname: String,
    @NonNull
    var name: String,
    @NonNull
    var price: Int,
    @NonNull
    var main: String,
)