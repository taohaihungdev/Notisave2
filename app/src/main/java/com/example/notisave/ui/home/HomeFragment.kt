package com.example.notisave.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notisave.MainActivity
import com.example.notisave.R
import com.example.notisave.databinding.FragmentHomeBinding
import com.example.notisave.model.NotificationEntity
import com.example.notisave.viewmodel.NotificationViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var notificationViewModel: NotificationViewModel
    private  lateinit var homeAdapter: HomeAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).handleShowBottomNavigation(true)
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerview()
        dataNotification()
    }

    private fun setUpRecyclerview() {
       homeAdapter = HomeAdapter()
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false).apply {
                reverseLayout = true // Sắp xếp ngược từ cuối lên đầu
                stackFromEnd = true
            }
        binding.rvHome.apply {
            adapter = homeAdapter
            setLayoutManager(layoutManager)
        }
    }

    private fun dataNotification() {

        // Khởi tạo ViewModel
        val notificationViewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)
        notificationViewModel.allNotifications.observe(viewLifecycleOwner, Observer { notifications ->
            val uniquePackageName = mutableListOf<String>()
            val uniqueNotifications = mutableListOf<NotificationEntity>()
            for (notification in notifications) {
                // Chỉ thêm packageName nếu chưa tồn tại trong danh sách
                if (!uniquePackageName.contains(notification.packageName)) {
                    notification.packageName?.let { uniquePackageName.add(it) }
                    uniqueNotifications.add(notification)
                }
            }
            homeAdapter.submitList(uniqueNotifications)
            Log.d("List", "onCreateView: ${notifications.size}")
        })
        homeAdapter.onItemClick ={notification ->
            Log.d("packagename", "dataNotification: ${notification.packageName}")
            val bundle = Bundle().apply {
                putString("packageName", notification.packageName)
                putByteArray("icon", notification.icon)
                putString("appName", notification.appName)
                putString("title", notification.title)
                putString("day", notification.day)
                putString("hour", notification.hour)
            }
//            val bundle = Bundle().apply {
//                putParcelable("notification", notification)
//            }
            val detailAppMessageFragment = DetailAppMessageFragment().apply {
                arguments = bundle
            }
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentView, detailAppMessageFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }
    }
}
