package com.example.notisave.ui.More.Setting

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.notisave.MainActivity
import com.example.notisave.R
import com.example.notisave.databinding.FragmentSettingBinding
import com.example.notisave.viewmodel.NotificationViewModel


class SettingFragment :Fragment() {
    lateinit var binding: FragmentSettingBinding
    private lateinit var viewModel: NotificationViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).handleShowBottomNavigation(false)
        binding= FragmentSettingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)
        handleSwitchCompat()
    }

    private fun handleSwitchCompat() {
        binding.switchCompatAccess.setOnCheckedChangeListener { buttonView, isChecked ->
            // Xử lý khi trạng thái của SwitchCompat thay đổi
            if (isChecked && !(activity as MainActivity).isNotificationServiceEnabled()) {
                startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
            } else {
                startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
            }
        }
//        binding.switchCompatPassCode.setOnCheckedChangeListener { buttonView, isChecked ->
//            // Xử lý khi trạng thái của SwitchCompat thay đổi
//            if (isChecked) {
//                val fragmentManager = requireActivity().supportFragmentManager
//                val fragmentTransaction = fragmentManager.beginTransaction()
//                val passCodeFragment = PassCodeFragment()
//                fragmentTransaction.replace(R.id.fragmentView,passCodeFragment)
//                fragmentTransaction.addToBackStack(null)
//                fragmentTransaction.commit()
//            } else {
//
//            }
//        }
        binding.tvClearData.setOnClickListener{
            viewModel.deleteAllNotifications()
        }

    }
}