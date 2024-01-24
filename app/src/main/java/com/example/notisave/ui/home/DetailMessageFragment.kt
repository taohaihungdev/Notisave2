package com.example.notisave.ui.home

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notisave.databinding.FragmentDetailAppMessageBinding
import com.example.notisave.model.NotificationEntity
import com.example.notisave.viewmodel.NotificationViewModel

class DetailMessageFragment :Fragment() {
    lateinit var binding: FragmentDetailAppMessageBinding
    private val displayedDates = mutableListOf<String>()
    private lateinit var detailMessageAdapter: DetailMessageAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding= FragmentDetailAppMessageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        setUpRecyclerview()
        setUpDaTa()
    }

    private fun setUpDaTa() {
        val packageName = arguments?.getString("packageName")
        val title = arguments?.getString("title")
        val notificationViewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)
        if (packageName != null) {
            if (title != null) {
                notificationViewModel.getNotificationsDetail(packageName, title).observe(viewLifecycleOwner) { notifications ->
                    updateRecyclerView(notifications)
                }
            }
        }
    }
    private fun updateRecyclerView(notifications: List<NotificationEntity>) {
        displayedDates.clear()

        val displayItems = mutableListOf<NotificationEntity>()
        for (notification in notifications) {
            val date = notification.day
            if (!displayedDates.contains(date)) {
                // Nếu ngày chưa tồn tại, cập nhật danh sách ngày đã hiển thị và thêm dữ liệu mới
                displayedDates.add(date)
                displayItems.add(NotificationEntity(notification.id,notification.packageName,notification.icon,notification.largeIcon,notification.appName,notification.title,notification.text,notification.day,notification.hour))
            } else {
                // Nếu ngày đã tồn tại, chỉ thêm dữ liệu mà không thêm ngày
                displayItems.add(NotificationEntity(notification.id,notification.packageName,notification.icon,notification.largeIcon,notification.appName,notification.title,notification.text,"",notification.hour))
            }
        }

        detailMessageAdapter.submitList(displayItems)
    }

    private fun setUpView() {
        val packageName = arguments?.getString("packageName")
        val icon = arguments?.getByteArray("icon")
        val appName = arguments?.getString("appName")
        val title = arguments?.getString("title")
        val day = arguments?.getLong("timestamp")
        val hour = arguments?.getLong("senderRealTime")
        val iconBitmap = BitmapFactory.decodeByteArray(icon, 0, icon!!.size)
        Log.d("AppName", "setUpView: $appName")
        binding.tvTitleNameApp.text = title
        binding.ivIcon.setImageBitmap(iconBitmap)  // Use setImageBitmap instead of setImageDrawable
    }

    private fun setUpRecyclerview() {
        detailMessageAdapter = DetailMessageAdapter()
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvListMessage.apply {
            adapter = detailMessageAdapter
            setLayoutManager(layoutManager)
        }
    }
}