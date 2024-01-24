package com.example.notisave.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messsage")
data class MessageEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0, // Trường id tự động tăng
    val packageName: String,
    val title: String?,
    val text: String?,
    )