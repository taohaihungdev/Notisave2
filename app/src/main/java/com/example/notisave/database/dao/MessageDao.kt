package com.example.notisave.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

import com.example.notisave.model.MessageEntity

@Dao
interface MessageDao {
    @Transaction
    suspend fun insertIfNotExists(messgae: MessageEntity) {
        val existingCount = getNotificationCountByPackage(messgae.packageName)

        if (existingCount == 0) {
            insert(messgae)
        }
    }

    @Insert
    suspend fun insert(messgae: MessageEntity)

    @Query("SELECT COUNT(*) FROM messsage WHERE packageName = :packageName")
    suspend fun getNotificationCountByPackage(packageName: String): Int
//    @Insert
//    suspend fun insert(notification: NotificationEntity)

    @Query("SELECT * FROM messsage")
    fun getAllNotificationsLiveData(): LiveData<List<MessageEntity>>


}

