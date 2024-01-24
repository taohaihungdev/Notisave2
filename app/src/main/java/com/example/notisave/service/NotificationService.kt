package com.example.notisave.service
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import com.example.notisave.R
import com.example.notisave.model.AppNameEntity
import com.example.notisave.model.MessageEntity
import com.example.notisave.model.NotificationEntity
import com.example.notisave.viewmodel.NotificationViewModel
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class NotificationService : NotificationListenerService() {
    private lateinit var notificationViewModel: NotificationViewModel
    companion object {
        var isServiceEnabled = true
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                "ENABLE_SERVICE" -> {
                    isServiceEnabled = true
                    Log.d("ServiceStatus", "Service is enabled")
                }
                "DISABLE_SERVICE" -> {
                    isServiceEnabled = false
                    Log.d("ServiceStatus", "Service is disabled")
                }

                else -> {}
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        Log.d("isServiceEnabled", "disableService: $isServiceEnabled")
        super.onCreate()
        notificationViewModel = ViewModelProvider.AndroidViewModelFactory(application)
            .create(NotificationViewModel::class.java)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)
        handleNotification(sbn)
    }

    fun enableService()  {
        isServiceEnabled =true

    }

    fun disableService()  {
        isServiceEnabled = false
    }


    private fun handleNotification(sbn: StatusBarNotification) {
        if(!isServiceEnabled){
            return
        }
        val notification = sbn.notification
        val pkg = sbn.packageName
        val icon = notification.smallIcon  // Icon của thông báo
        val title = notification.extras.getString("android.title")
        val text = notification.extras.getCharSequence("android.text")
        val timestamp = sbn.postTime  // Thời gian xuất hiện thông báo
        val senderRealTime = System.currentTimeMillis()  // Thời gian hiện tại

        val date = Date(timestamp)
        val format = SimpleDateFormat("E - dd/MM/yyyy", Locale.getDefault())
        val formattedDateTime = format.format(date)

        val hour = Date(senderRealTime)
        val formatHour = SimpleDateFormat("hh:mma", Locale.getDefault())
        val formattedHourTime = formatHour.format(hour)

        val largeIcon: Bitmap? = (notification.getLargeIcon() ?: getDefaultLargeIcon())?.let {
            when (it) {
                is Bitmap -> it
                is Icon -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        val width = 60 // Độ rộng mong muốn, bạn có thể thay đổi giá trị này.
                        val height = 60 // Chiều cao mong muốn, bạn có thể thay đổi giá trị này.
                        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                        val canvas = Canvas(bitmap)
                        // Vẽ trực tiếp từ Icon lên Canvas
                        it.loadDrawable(applicationContext)?.draw(canvas)

                        bitmap
                    } else {
                        // Xử lý cho các phiên bản Android cũ hơn
                        val width = 60 // Độ rộng mong muốn, bạn có thể thay đổi giá trị này.
                        val height = 60 // Chiều cao mong muốn, bạn có thể thay đổi giá trị này.
                        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                        val canvas = Canvas(bitmap)

                        // Vẽ trực tiếp từ Icon lên Canvas
                        it.loadDrawable(applicationContext)?.apply {
                            setBounds(0, 0, canvas.width, canvas.height)
                            draw(canvas)
                        }

                        bitmap
                    }
                }

                else -> getDefaultLargeIcon()
            }
        }




        // Chuyển đổi Bitmap thành mảng byte
        val largeIconByteArray = convertBitmapToByteArrayLargeIcon(largeIcon!!)
        val packageManager = packageManager
        try {
            val appInfo = packageManager.getApplicationInfo(pkg, 0)
            val appIcon = packageManager.getApplicationIcon(appInfo)
            val appName = getApplicationName(pkg, applicationContext)
            // Chuyển đổi Drawable thành mảng byte
            val iconByteArray = convertDrawableToByteArray(appIcon)

            val notificationEntity = NotificationEntity(
                packageName = pkg,
                icon = iconByteArray,
                largeIcon = largeIconByteArray,
                appName = appName,
                title = title,
                text = text?.toString(),
                day = formattedDateTime,
                hour = formattedHourTime
            )
//            val appNameEntity = AppNameEntity(
//                packageName = pkg,
//                icon = iconByteArray,
//                appName = appName
//            )

            val messageEntity = MessageEntity(
                packageName = pkg,
                title = title,
                text = text?.toString(),
            )
            Log.d("NotificationListener", "New Notification - pkg: $pkg - Icon: $icon - Title: $title, Text: $text" +
                    ", Icon $icon, LargeIcon $largeIcon")
            // Lưu vào Room Database qua ViewModel
            notificationViewModel.insert(notificationEntity)

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun getDefaultLargeIcon(): Bitmap {
        return BitmapFactory.decodeResource(resources, R.drawable.settings)
    }

    private fun getApplicationName(packageName: String, context: Context): String {
        val packageManager = context.packageManager
        val applicationInfo: ApplicationInfo?

        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            return ""
        }

        return packageManager.getApplicationLabel(applicationInfo).toString()
    }

    private fun convertDrawableToByteArray(drawable: Drawable): ByteArray {
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private fun convertBitmapToByteArrayLargeIcon(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
}




