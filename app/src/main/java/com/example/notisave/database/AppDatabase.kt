package com.example.notisave.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notisave.database.dao.AppInfoDao
import com.example.notisave.database.dao.MessageDao
import com.example.notisave.database.dao.NotificationDao
import com.example.notisave.model.AppNameEntity
import com.example.notisave.model.MessageEntity
import com.example.notisave.model.NotificationEntity

@Database(entities = [NotificationEntity::class ,
    AppNameEntity::class, MessageEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun notificationDao(): NotificationDao
    abstract fun messageDao() :MessageDao
    abstract fun appDao() :AppInfoDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "notification-database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}


