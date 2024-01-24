package com.example.notisave.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.notisave.database.AppDatabase
import com.example.notisave.model.NotificationEntity
import com.example.notisave.database.repository.NotificationRepository
import com.example.notisave.model.AppNameEntity
import com.example.notisave.model.MessageEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NotificationRepository

    val allNotifications: LiveData<List<NotificationEntity>>
    val allApp: LiveData<List<AppNameEntity>>
    private val _searchResults = MutableLiveData<List<AppNameEntity>>()
    val searchResults: LiveData<List<AppNameEntity>> get() = _searchResults


    init {
        val notificationDao = AppDatabase.getInstance(application).notificationDao()
        val appNameDao = AppDatabase.getInstance(application).appDao()
        val messageDao = AppDatabase.getInstance(application).messageDao()
        repository = NotificationRepository(notificationDao, messageDao, appNameDao)
        allNotifications = repository.allNotifications
        allApp = repository.allApp

    }

    fun insert(notification: NotificationEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(notification)
    }

    fun insertAppName(appName: AppNameEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertApp(appName)
    }

    fun getAllApps(): LiveData<List<AppNameEntity>> {
        return repository.allApp
    }


    fun insertMessage(message: MessageEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertMessage(message)
    }

    fun getNotificationsGroup() = liveData(Dispatchers.IO) {
        try {
            emit(repository.getNotificationsGroup())
        } catch (e: Exception) {
            Log.e("NotificationViewModel", e.toString())
        }
    }


    fun getNotificationsByPackage(packageName: String) = liveData(Dispatchers.IO) {
        try {
            emit(repository.getNotificationsByPackage(packageName))
        } catch (e: Exception) {
            Log.e("NotificationViewModel", e.toString())
        }
    }

    fun getNotificationsDetail(packageName: String, title: String) = liveData(Dispatchers.IO) {
        try {
            emit(repository.getNotificationsDetail(packageName, title))
        } catch (e: Exception) {
            Log.e("NotificationViewModel", e.toString())
        }
    }
    fun updateAllApps(isChecked: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateAllApps(isChecked)
        }
    }
    fun updateApp(app: AppNameEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateApp(app)
        }
    }
    fun searchApps(query: String) {
        repository.searchApps(query).observeForever {
            _searchResults.value = it
        }
    }

//    fun searchApps(query: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.searchApps(query)
//        }
//    }

    fun deleteAllNotifications() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllNotifications()
        }
    }
    fun deleteApps(packageName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteApp(packageName)
        }
    }

}



