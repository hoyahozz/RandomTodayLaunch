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
    @NonNull
    var type : String,
    @NonNull
    var name : String,
)
//Expected:
//TableInfo{name='food', columns={
//    name=Column{name='name', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0, defaultValue='null'},
//    type=Column{name='type', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0, defaultValue='null'},
//    id=Column{name='id', type='INTEGER', affinity='3', notNull=false, primaryKeyPosition=1, defaultValue='null'}}, foreignKeys=[], indices=[]}
//Found:
//TableInfo{name='food', columns={
//    name=Column{name='name', type='TEXT', affinity='2', notNull=false, primaryKeyPosition=0, defaultValue='null'},
//    id=Column{name='id', type='INTEGER', affinity='3', notNull=false, primaryKeyPosition=1, defaultValue='null'},
//    type=Column{name='type', type='TEXT', affinity='2', notNull=false, primaryKeyPosition=0, defaultValue='null'}}, foreignKeys=[], indices=[]}