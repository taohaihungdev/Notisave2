package com.example.notisave.broadcast_receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.notisave.database.dao.AppInfoDao

class InstallUninstallReceiver : BroadcastReceiver() {
    lateinit var appDao: AppInfoDao
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_PACKAGE_ADDED -> {
                val packageName = intent.data?.encodedSchemeSpecificPart
                Log.d("PackageChangeReceiver", "Package installed: $packageName")
                // Xử lý khi có ứng dụng được cài đặt
            }
            Intent.ACTION_PACKAGE_REMOVED -> {
                val packageName = intent.data?.encodedSchemeSpecificPart
                Log.e("PackageChangeReceiver", "Package removed: $packageName")
                // Xử lý khi có ứng dụng bị gỡ bỏ
            }
        }
    }
}
