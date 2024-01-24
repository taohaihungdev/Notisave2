package com.example.notisave.model


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "appname")
data class AppNameEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val appName: String,
    val packageName: String,
    val icon: ByteArray,
    var isCheck: Boolean
)