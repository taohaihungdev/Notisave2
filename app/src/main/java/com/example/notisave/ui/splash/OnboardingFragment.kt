package com.example.notisave.ui.splash


import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.notisave.MainActivity
import com.example.notisave.R
import com.example.notisave.databinding.FragmentOnboardingBinding



class OnboardingFragment :Fragment() {
    lateinit var binding: FragmentOnboardingBinding
    private val introSliderAdapter = IntroSliderAdapter(
        listOf(
            IntroSlide(
                "Lưu thông báo",
                "Thông báo và tin nhắn sẽ được lưu",
                R.drawable.amico
            ),
            IntroSlide(
                "Xem lại tin nhắn đã lưu",
                "Bạn có thể chọn ứng dụng để xem tin nhắn đã lưu",
                R.drawable.pana
            )

        )
    )
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).handleShowBottomNavigation(false)
        binding= FragmentOnboardingBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleDataSlider()
        handleButtonNext()
        handleTextSkipIntro()



    }

    private fun handleTextSkipIntro() {
        binding.textSkipIntro.setOnClickListener {
            if(!(activity as MainActivity).isNotificationServiceEnabled()){
                startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
            }
            else{
                findNavController().navigate(R.id.action_onboardingFragment_to_homeFragment)
            }


        }
    }

    private fun handleDataSlider() {
        binding.introSliderViewPager.adapter = introSliderAdapter
        setupIndicators()
        setCurrentIndicator(0)
        binding.introSliderViewPager.registerOnPageChangeCallback(object :
        ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })
    }

    private fun setCurrentIndicator(index: Int) {
        val chilCount = binding.indicatorContainer.childCount
        for (i in 0 until chilCount) {
            val imageView = binding.indicatorContainer[i] as ImageView
            if (i == index) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.indicator_inactive
                    )
                )
            }
        }

    }

    private fun setupIndicators() {
        val indicators= arrayOfNulls<ImageView>(introSliderAdapter.itemCount)
        val layoutParams :LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        layoutParams.setMargins(8,0,8,0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(requireContext())
            indicators[i].apply {
                this?.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.indicator_inactive
                    )

                )
                this?.layoutParams = layoutParams
            }

            binding.indicatorContainer.addView(indicators[i])
        }
    }

    private fun handleButtonNext() {
        binding.buttonNext.setOnClickListener{
            if(binding.introSliderViewPager.currentItem+1< introSliderAdapter.itemCount){
                binding.introSliderViewPager.currentItem+=1
            }
            else if(!(activity as MainActivity).isNotificationServiceEnabled()){
                startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
            }
            else{
                findNavController().navigate(R.id.action_onboardingFragment_to_homeFragment)
            }

            }
        }


}



