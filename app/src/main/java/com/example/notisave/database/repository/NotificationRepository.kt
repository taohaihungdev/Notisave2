package com.example.notisave.database.repository


import androidx.lifecycle.LiveData
import com.example.notisave.database.dao.AppInfoDao
import com.example.notisave.database.dao.MessageDao
import com.example.notisave.database.dao.NotificationDao
import com.example.notisave.model.AppNameEntity
import com.example.notisave.model.MessageEntity
import com.example.notisave.model.NotificationEntity

class NotificationRepository(
    private val notificationDao: NotificationDao ,
    private val messageDao: MessageDao,
    private val appDao: AppInfoDao,
) {

    val allNotifications: LiveData<List<NotificationEntity>> = notificationDao.getAllNotificationsLiveData()
    val allApp: LiveData<List<AppNameEntity>> = appDao.getAll()


    suspend fun insert(notification: NotificationEntity) {
        notificationDao.insertNotification(notification)
    }
    suspend fun insertMessage(message: MessageEntity){
        messageDao.insert(message)
    }
    suspend fun insertApp(app: AppNameEntity){
        appDao.insertIfNotExists(app)
    }


    suspend fun getNotificationsByPackage(packageName: String): List<NotificationEntity> {
         return notificationDao.getNotificationsByPackage(packageName)
}

    suspend fun getAllNotificationsLiveData(): LiveData<List<NotificationEntity>> {
        return notificationDao.getAllNotificationsLiveData()
    }

    suspend fun getNotificationsDetail(packageName: String, title: String):List<NotificationEntity>{
        return notificationDao.getNotificationsDetail(packageName,title)
    }

    suspend fun getNotificationsGroup():List<NotificationEntity>{
        return notificationDao.getNotificationsGroup()
    }

    suspend fun updateAllApps(isChecked: Boolean) {
        appDao.updateAllApps(isChecked)
    }

     suspend fun updateApp(app: AppNameEntity) {
        appDao.updateApp(app)
     }
    fun searchApps(query: String): LiveData<List<AppNameEntity>> {
        return appDao.searchApps(query)
    }
    suspend fun deleteAllNotifications() {
        notificationDao.deleteAllNotifications()
    }
    suspend fun deleteApp(packageName: String){
        appDao.deleteApp(packageName)
    }



}
