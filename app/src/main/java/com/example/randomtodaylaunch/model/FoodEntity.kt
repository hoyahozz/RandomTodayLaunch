package com.example.randomtodaylaunch.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "food",
    indices = [Index(value = ["name"], unique = true)] // 유니크키 설정
)
data class FoodEntity(
    @PrimaryKey
    var id: Int?,

    @NonNull
    var type: String,

    @NonNull
    var name: String,
)

//Expected:
//TableInfo{name='food', columns={type=Column{name='type', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0, defaultValue='null'}, name=Column{name='name', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0, defaultValue='null'}, id=Column{name='id', type='INTEGER', affinity='3', notNull=false, primaryKeyPosition=1, defaultValue='null'}}, foreignKeys=[], indices=[Index{name='index_food_name', unique=true, columns=[name], orders=[ASC]}]}
//Found:
//TableInfo{name='food', columns={name=Column{name='name', type='TEXT', affinity='2', notNull=false, primaryKeyPosition=0, defaultValue='null'}, id=Column{name='id', type='INTEGER', affinity='3', notNull=false, primaryKeyPosition=1, defaultValue='null'}, type=Column{name='type', type='TEXT', affinity='2', notNull=false, primaryKeyPosition=0, defaultValue='null'}}, foreignKeys=[], indices=[Index{name='index_food_name', unique=true, columns=[name], orders=[ASC]}]}

