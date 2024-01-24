package com.example.notisave

import android.content.ComponentName
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.notisave.broadcast_receiver.InstallUninstallReceiver
import com.example.notisave.databinding.ActivityMainBinding
import com.example.notisave.model.AppNameEntity
import com.example.notisave.ui.Message.GroupMessageFragment
import com.example.notisave.ui.More.MoreFragment
import com.example.notisave.ui.home.HomeFragment
import com.example.notisave.viewmodel.NotificationViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var binding: ActivityMainBinding
    private val ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners"
    private var enableNotificationListenerAlertDialog:android.app.AlertDialog? = null
    private lateinit var viewModel: NotificationViewModel
    private var navController : NavController?=null
    private lateinit var notificationFragment: HomeFragment
    private lateinit var messageFragment: GroupMessageFragment
    private lateinit var moreFragment: MoreFragment
    private var currentItem: Int = 0
    private var receiver: InstallUninstallReceiver? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        getAllInstalledApps()
        setContentView(binding.root)
        initView()
        setUpNavigationFragment()
        receiver = InstallUninstallReceiver()

    }


    private fun formatTime(milliseconds: Long): String {
        val hours = (milliseconds / 3600000).toInt()
        val minutes = ((milliseconds % 3600000) / 60000).toInt()
        val seconds = ((milliseconds % 60000) / 1000).toInt()

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
    private fun getAllInstalledApps(): List<AppNameEntity> {
        val packageManager: PackageManager = packageManager
        val apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        val appList = mutableListOf<AppNameEntity>()

        for (app in apps) {
            val packageName = app.packageName
            if ((app.flags and ApplicationInfo.FLAG_SYSTEM) == 0 || isWhitelistedApp(packageName)) {
                val appName = packageManager.getApplicationLabel(app).toString()
                val appIcon = packageManager.getApplicationIcon(app)
                val iconByteArray = getIconByteArray(packageManager, app) 
                val isCheck = false // Giả sử ban đầu không chọn

                val appInfo = AppNameEntity(appName = appName, packageName = packageName, icon = iconByteArray, isCheck = isCheck)
                appList.add(appInfo)
                Log.d("ListApp", "getAllInstalledApps: ${appName}")
                viewModel.insertAppName(appInfo)
            }

        }
        // Sắp xếp danh sách theo aplab (tên ứng dụng)
        val sortedList = appList.sortedBy { it.appName }

        return sortedList
    }

    fun getIconByteArray(packageManager: PackageManager, app: ApplicationInfo): ByteArray {
        val drawable = packageManager.getApplicationIcon(app)
        val bitmap = drawableToBitmap(drawable)
        return bitmapToByteArray(bitmap)
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private fun isWhitelistedApp(packageName: String): Boolean {
        // Thêm các tên gói của các ứng dụng bạn muốn giữ lại vào đây
        val whitelistedApps = setOf("com.android.phone", "com.android.mms")
        return whitelistedApps.contains(packageName)
    }



    private fun setUpNavigationFragment() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentView) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun initView() {
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(this)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.group_message -> {
                showFragment(GroupMessageFragment())
                return true
            }
            R.id.notification -> {
                showFragment(HomeFragment())
                return true
            }
            R.id.more -> {
                showFragment(MoreFragment())
                return true
            }
        }
        return false
    }
    private fun showFragment(fragment: Fragment) {
        val tag = fragment.javaClass.simpleName
        val existingFragment = supportFragmentManager.findFragmentByTag(tag)

        if (existingFragment != null) {
            // Nếu Fragment đã tồn tại, chỉ hiển thị lại nó mà không thêm mới
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentView, existingFragment, tag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
        } else {
            // Nếu Fragment chưa tồn tại, thêm mới và hiển thị
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentView, fragment, tag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
        }
    }


    fun isNotificationServiceEnabled(): Boolean {
        val pkgName: String = packageName
        val flat = Settings.Secure.getString(
            getContentResolver(),
            ENABLED_NOTIFICATION_LISTENERS
        )
        if (!TextUtils.isEmpty(flat)) {
            val names = flat.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            for (i in names.indices) {
                val cn = ComponentName.unflattenFromString(names[i])
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.packageName)) {
                        return true
                    }
                }
            }
        }
        return false
    }




    fun handleShowBottomNavigation(isShow: Boolean) {
        binding.bottomNavigationView.visibility =
            if (isShow)
                View.VISIBLE
            else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        val currentTime = System.currentTimeMillis()
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = preferences.edit()
        editor.putLong("lastClosedTime", currentTime)
        Log.d("timeDifference", "lastClosedTime: $currentTime")
        editor.apply()
    }

    private fun saveExitTime(exitTime: Long) {
        val sharedPreferences = getSharedPreferences("countdown_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putLong("exit_time", exitTime)
        editor.apply()
    }


}
