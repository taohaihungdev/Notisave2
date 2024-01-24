package com.example.notisave.database.dao


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.notisave.model.AppNameEntity



@Dao
interface AppInfoDao {

    @Query("SELECT * FROM appname")
    fun getAll():LiveData<List<AppNameEntity>>

    @Insert
    fun insertApp(app: AppNameEntity)

    @Transaction
    suspend fun insertIfNotExists(app: AppNameEntity) {
        val existingCount = getAppCount(app.packageName)

        if (existingCount == 0) {
            insertApp(app)
        }
    }

    @Query("SELECT COUNT(*) FROM appname WHERE packageName = :packageName")
    suspend fun getAppCount(packageName: String): Int


    @Query("UPDATE appname SET isCheck = :isChecked")
    suspend fun updateAllApps(isChecked: Boolean)

    @Update
    suspend fun updateApp(app: AppNameEntity)

    @Query("SELECT * FROM appname WHERE appName LIKE :query || '%'")
    fun searchApps(query: String): LiveData<List<AppNameEntity>>

    @Query("DELETE  FROM appname WHERE packageName = :packageName")
     fun deleteApp(packageName: String)


}