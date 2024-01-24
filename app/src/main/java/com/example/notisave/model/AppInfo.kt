package com.example.notisave.model


import android.graphics.drawable.Drawable
import androidx.room.Entity
import androidx.room.PrimaryKey

data class AppInfo(
    val appName: String,
    val appIcon: Drawable,
    val packageName: String
)

//@Entity(tableName = "app")
//data class AppInfo(
//    @PrimaryKey(autoGenerate = true)
//    val id: Long = 0,
//    val appName: String,
//    val packageName: String,
//    val icon: ByteArray,
//    val isCheck: Boolean
//)
