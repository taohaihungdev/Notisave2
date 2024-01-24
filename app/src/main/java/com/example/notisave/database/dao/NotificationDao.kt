package com.example.notisave.database.dao



import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.notisave.model.NotificationEntity

@Dao
interface NotificationDao {
    @Transaction
    suspend fun insertIfNotExists(notification: NotificationEntity) {
        val existingCount = getNotificationCountByPackage(notification.packageName)

        if (existingCount == 0) {
            insert(notification)
        }
    }
    suspend fun insertNotification(notification: NotificationEntity) {
        if (notification.title.isNullOrEmpty() || notification.text.isNullOrEmpty()) {
            // Xử lý khi title hoặc text trống, có thể thông báo lỗi hoặc thực hiện hành động khác tùy thuộc vào yêu cầu của bạn.
        } else {
            insert(notification)
        }
    }


    @Insert
    suspend fun insert(notification: NotificationEntity)

    @Query("SELECT COUNT(*) FROM notifications WHERE packageName = :packageName")
    suspend fun getNotificationCountByPackage(packageName: String): Int
//    @Insert
//    suspend fun insert(notification: NotificationEntity)

    @Query("SELECT * FROM notifications")
    fun getAllNotificationsLiveData(): LiveData<List<NotificationEntity>>

    @Query("SELECT * FROM notifications WHERE packageName = :packageName")
    suspend fun getNotificationsByPackage(packageName: String): List<NotificationEntity>

    @Query("SELECT * FROM notifications WHERE packageName = :packageName and title = :title")
    suspend fun getNotificationsDetail(packageName: String, title: String): List<NotificationEntity>

    @Query("SELECT * FROM notifications inner join appname on notifications.packageName =appname.packageName WHERE isCheck = 1 ")
    suspend fun getNotificationsGroup(): List<NotificationEntity>

    @Query("DELETE FROM notifications")
    suspend fun deleteAllNotifications()

}
