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
import com.example.notisave.MainActivity
import com.example.notisave.R
import com.example.notisave.databinding.FragmentDetailAppMessageBinding
import com.example.notisave.model.NotificationEntity
import com.example.notisave.viewmodel.NotificationViewModel



class DetailAppMessageFragment :Fragment() {
    lateinit var binding: FragmentDetailAppMessageBinding
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var notificationViewModel: NotificationViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).handleShowBottomNavigation(false)
        binding = FragmentDetailAppMessageBinding.inflate(layoutInflater)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//
        setUpView()
        setUpRecyclerview()
        setUpDaTa()


    }

    private fun setUpRecyclerview() {
        messageAdapter = MessageAdapter()
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false).apply {
                reverseLayout = true // Sắp xếp ngược từ cuối lên đầu
                stackFromEnd = true
            }
        binding.rvListMessage.apply {
            adapter = messageAdapter
            setLayoutManager(layoutManager)
        }
    }

    private fun setUpView() {
        val packageName = arguments?.getString("packageName")
        val icon = arguments?.getByteArray("icon")
        val appName = arguments?.getString("appName")
        val title = arguments?.getString("title")
        val day = arguments?.getString("day")
        val hour = arguments?.getString("hour")
        val iconBitmap = BitmapFactory.decodeByteArray(icon, 0, icon!!.size)
        Log.d("AppName", "setUpView: $appName")
        binding.tvTitleNameApp.text = appName
        binding.ivIcon.setImageBitmap(iconBitmap)  // Use setImageBitmap instead of setImageDrawable

    }

    private fun setUpDaTa() {
        val packageName = arguments?.getString("packageName")
        val notificationViewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)
        if (packageName != null) {
            notificationViewModel.getNotificationsByPackage(packageName).observe(viewLifecycleOwner) { notifications ->
                val uniqueTitles = mutableListOf<String>()
                val uniqueNotifications = mutableListOf<NotificationEntity>()
                for (notification in notifications) {
                    if (!uniqueTitles.contains(notification.title)) {
                        notification.title?.let { uniqueTitles.add(it) }
                        val truncatedTitle = notification.text?.take(50) ?: ""
                        notification.text = if (notification.text?.length ?: 0 > 50) "$truncatedTitle..." else truncatedTitle

                        uniqueNotifications.add(notification)
                    }
                }

                messageAdapter.submitList(uniqueNotifications)
            }
            messageAdapter.onItemClick ={notification ->
                Log.d("packagename", "dataNotification: ${notification.packageName}")
                val bundle = Bundle().apply {
                    putString("packageName", notification.packageName)
                    putByteArray("icon", notification.icon)
                    putString("appName", notification.appName)
                    putString("title", notification.title)
                    putString("day", notification.day)
                    putString("hour", notification.hour)
                }
                val detailMessageFragment = DetailMessageFragment().apply {
                    arguments = bundle
                }
                val fragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragmentView, detailMessageFragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
        }
    }

}