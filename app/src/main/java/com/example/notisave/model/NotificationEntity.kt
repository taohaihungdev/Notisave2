package com.example.notisave.model



import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val packageName: String,
    val icon: ByteArray,
    val largeIcon: ByteArray?,
    val appName: String?,
    var title: String?,
    var text: String?,
    val day: String,
    val hour: String
)



