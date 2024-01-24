package com.example.notisave.database.repository


import androidx.lifecycle.LiveData
import com.example.notisave.database.dao.AppInfoDao
import com.example.notisave.model.AppNameEntity
import com.example.notisave.model.NotificationEntity


class AppInfoRepository(private val appDao: AppInfoDao) {

    val allApp: LiveData<List<AppNameEntity>> = appDao.getAll()
     fun insert(app: AppNameEntity) {
        appDao.insertApp(app)
    }
    suspend fun getAllAppLiveData(): LiveData<List<AppNameEntity>> {
        return appDao.getAll()
    }
}