package com.example.notisave.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.notisave.MainActivity
import com.example.notisave.R
import com.example.notisave.databinding.FragmentSplashBinding
import com.example.notisave.ui.Message.SearchFragment
import com.example.notisave.ui.home.HomeFragment

class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        (activity as MainActivity).handleShowBottomNavigation(false)
        setUpListener()
        return binding.root
    }

    private fun setUpListener() {
        // Sử dụng handler của Fragment thay vì tạo một handler mới
        if((activity as MainActivity).isNotificationServiceEnabled()){
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
            }, 1000)
        }
        else{
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_splashFragment_to_onboardingFragment)
            }, 1000)
        }
    }
}
