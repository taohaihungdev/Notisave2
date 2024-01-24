package com.example.notisave

import android.app.Application
import androidx.room.Room
import com.example.notisave.database.AppDatabase

class ApplicationClass : Application() {
    companion object {
        lateinit var db: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "notification-database"
        ).build()
    }
}
