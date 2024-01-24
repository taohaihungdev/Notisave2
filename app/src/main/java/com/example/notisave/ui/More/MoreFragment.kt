package com.example.notisave.ui.More

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notisave.MainActivity
import com.example.notisave.R
import com.example.notisave.databinding.FragmentMoreBinding
import com.example.notisave.service.NotificationService
import com.example.notisave.ui.Message.SearchFragment
import com.example.notisave.ui.More.Setting.SettingFragment
import com.example.notisave.ui.More.apdater.MoreAdapter
import com.example.notisave.ui.More.model.MoreModel

class MoreFragment :Fragment() {
    lateinit var binding: FragmentMoreBinding
    private var listMore: List<MoreModel>? = null
    private var moreAdapter: MoreAdapter? = null
    private var noti :NotificationService = NotificationService()
    private lateinit var countDownTimer: CountDownTimer
    private var totalTimeInMillis: Long =   15*1000
    private val countDownInterval: Long = 1000 // 1 second
    private lateinit var handler: Handler
    var previousProgress = 0
    private var lastClosedTime :Long =0
    private var currentTime :Long =0
    private var lastOpenedTime:Long =0
    private var timeDifference :Long =0
    private var current =0
    private var isCardViewClicked = false
    private var isFirstRun = true


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).handleShowBottomNavigation(true)
        binding = FragmentMoreBinding.inflate(layoutInflater)
        currentTime = System.currentTimeMillis()
        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        lastOpenedTime = preferences.getLong("lastOpenedTime", 0)
        lastClosedTime = preferences.getLong("lastClosedTime", 0)
        current = preferences.getInt("current_progress", 0)

        if(lastClosedTime.toInt()==0){
            timeDifference =0
        }
        else{
            timeDifference = currentTime - lastClosedTime
        }
        Log.d("timeDifference", "timeDifference Fragment: $${formatTime(timeDifference)}")
        Log.d("timeDifference", "lastClosedTime Fragment: ${formatTime(lastClosedTime)}")
        Log.d("timeDifference", "currentTime Fragment: ${formatTime(currentTime)}")
        Log.d("timeDifference", "currentTime Fragment: $timeDifference")
        Log.d("timeDifference", "currentProgress Fragment: $current")
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putLong("lastOpenedTime", currentTime)
        Log.d("lastOpenedTime", "$lastOpenedTime")
        editor.apply()
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleDataFilm()
        val savedProgress = restoreSavedProgress()
        updateUI(savedProgress)
        handler = Handler(Looper.getMainLooper())
        binding.progressBar.max=totalTimeInMillis.toInt()
        if(timeDifference>totalTimeInMillis){
            binding.tvHour.text="0"
            binding.progressBar.progress=0
            val dialog = Dialog(requireContext())
            var view= layoutInflater.inflate(R.layout.dialog_add_group,null)
            var btnCancel = view.findViewById<AppCompatButton>(R.id.btnCancel)
            var btnOk = view.findViewById<AppCompatButton>(R.id.btnOk)
            dialog.setContentView(view)
            btnCancel.setOnClickListener {
                dialog.dismiss()
            }
            btnOk.setOnClickListener {
                dialog.dismiss()
            }
            dialog.setOnShowListener {
                val width = ViewGroup.LayoutParams.MATCH_PARENT
                val height = ViewGroup.LayoutParams.WRAP_CONTENT
                dialog.window?.setLayout(width, height)
            }
            dialog.setCancelable(true)
            dialog.setContentView(view)
            dialog.show()
        }
        else{
            startCountdownTimerClick()
        }
        if (isFirstRun) {
            startCountdownTimerClick()
            isFirstRun = false
        } else {

            Toast.makeText(requireContext(), "Hàm đã được chạy trước đó!", Toast.LENGTH_SHORT).show()
        }
    }
    private fun startCountdownTimerClick() {
        countDownTimer = object : CountDownTimer(totalTimeInMillis, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                var currentProgress = (totalTimeInMillis - millisUntilFinished).toInt()
                val calculatedProgress = current - currentProgress - timeDifference.toInt()
                if(current!=totalTimeInMillis.toInt() && current!=0){
                    if(timeDifference>totalTimeInMillis ){
                        binding.progressBar.progress =0
                        binding.tvHour.text = "Miễn phí"
                        if (isAdded) { // Kiểm tra xem Fragment có được gắn vào Activity không
                            val disableServiceIntent = Intent(requireContext(), NotificationService::class.java)
                            disableServiceIntent.action = "DISABLE_SERVICE"
                            requireContext().startService(disableServiceIntent)
                            val dialog = Dialog(requireContext())
                            var view= layoutInflater.inflate(R.layout.dialog_add_group,null)
                            var btnCancel = view.findViewById<AppCompatButton>(R.id.btnCancel)
                            var btnOk = view.findViewById<AppCompatButton>(R.id.btnOk)
                            dialog.setContentView(view)
                            btnCancel.setOnClickListener {
                                dialog.dismiss()
                            }
                            btnOk.setOnClickListener {
                                dialog.dismiss()
                                requireActivity().onBackPressed()
                            }
                            dialog.setOnShowListener {
                                val width = ViewGroup.LayoutParams.MATCH_PARENT
                                val height = ViewGroup.LayoutParams.WRAP_CONTENT
                                dialog.window?.setLayout(width, height)
                            }
                            dialog.setCancelable(true)
                            dialog.setContentView(view)
                            dialog.show()
                            return




                        }
                    }
                        else if(calculatedProgress>0  ){
                            binding.progressBar.progress= calculatedProgress
                            binding.tvHour.text=formatTime(calculatedProgress.toLong())
                        Log.d("Ontick", "onTick: ${formatTime(calculatedProgress.toLong())}")
                        }
                        else if(calculatedProgress <=0){
                            binding.progressBar.progress=0
                            binding.tvHour.text = "Miễn phí"
                        val dialog = Dialog(requireContext())
                        var view= layoutInflater.inflate(R.layout.dialog_add_group,null)
                        var btnCancel = view.findViewById<AppCompatButton>(R.id.btnCancel)
                        var btnOk = view.findViewById<AppCompatButton>(R.id.btnOk)
                        dialog.setContentView(view)
                        btnCancel.setOnClickListener {
                            dialog.dismiss()
                        }
                        btnOk.setOnClickListener {
                            dialog.dismiss()
                            requireActivity().onBackPressed()
                        }
                        dialog.setOnShowListener {
                            val width = ViewGroup.LayoutParams.MATCH_PARENT
                            val height = ViewGroup.LayoutParams.WRAP_CONTENT
                            dialog.window?.setLayout(width, height)
                        }
                        dialog.setCancelable(true)
                        dialog.setContentView(view)
                        dialog.show()

                        }
                }
                else{
                    binding.progressBar.progress = (totalTimeInMillis- currentProgress -timeDifference).toInt()
                    binding.tvHour.text = formatTime(totalTimeInMillis - currentProgress.toLong()-timeDifference)
                }
                saveCurrentProgress(currentProgress)
            }

            override fun onFinish() {
                binding.progressBar.max=0
                if (isAdded) { // Kiểm tra xem Fragment có được gắn vào Activity không
                    val disableServiceIntent = Intent(requireContext(), NotificationService::class.java)
                    disableServiceIntent.action = "DISABLE_SERVICE"
                    requireContext().startService(disableServiceIntent)
                }


            }
        }
        countDownTimer.start()
        // Cập nhật tiến trình mỗi giây bằng cách sử dụng Handler
        handler.postDelayed(object : Runnable {
            override fun run() {
//                binding.progressBar.progress = currentProgress
                handler.postDelayed(this, countDownInterval)

            }
        }, countDownInterval)
    }

    private fun updateUI(progress: Int) {
        binding.progressBar.progress = progress
        binding.tvHour.text = formatTime((totalTimeInMillis - progress).toLong())
    }
    private fun restoreSavedProgress(): Int {
        val sharedPreferences = context?.getSharedPreferences("countdown_prefs", Context.MODE_PRIVATE)
        return sharedPreferences!!.getInt("current_progress", 0)
    }

    override fun onStop() {
        val currentProgress = binding.progressBar.progress
        saveCurrentProgress(currentProgress)
        val lastClosedTime = System.currentTimeMillis()
        val preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val editor = preferences.edit()
        editor.putLong("lastClosedTime", lastClosedTime)
        editor.putInt("current_progress", currentProgress)
        Log.d("timeDifference", "lastClosedTime: $lastClosedTime")
        editor.apply()
        super.onStop()
    }
    override fun onDestroy() {
        super.onDestroy()

    }
     fun formatTime(milliseconds: Long): String {
        val hours = (milliseconds / 3600000).toInt()
        val minutes = ((milliseconds % 3600000) / 60000).toInt()
        val seconds = ((milliseconds % 60000) / 1000).toInt()
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
    private fun saveCurrentProgress(currentProgress: Int) {
        val sharedPreferences = context?.getSharedPreferences("countdown_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putInt("current_progress", currentProgress)
        editor?.apply()
    }

    private fun handleDataFilm() {
        listMore = listOf(
            MoreModel(R.drawable.icon_svg_edit, "Chỉnh sửa nhóm"),
            MoreModel(R.drawable.icon_svg_setting, "Cài đặt")
        )
        binding.rvMore.apply {
            var layoutParams =
                LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)
            layoutManager = layoutParams
            moreAdapter = MoreAdapter(listMore)

            adapter = moreAdapter
        }
        moreAdapter?.onClickItem = { more ->
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()

            when (more.nameMore) {
                "Chỉnh sửa nhóm" -> {
                    val blockNotificationFragment = BlockNotificationFragment()
                    fragmentTransaction.replace(R.id.fragmentView,blockNotificationFragment)
//                    val editGroupFragment = EditGroupFragment()
//                    fragmentTransaction.replace(R.id.fragmentView, editGroupFragment)
//                    val disableServiceIntent = Intent(requireContext(), NotificationService::class.java)
//                    disableServiceIntent.action = "DISABLE_SERVICE"
//                    requireContext().startService(disableServiceIntent)
//                    Log.d("disableService", "${noti.disableService()}")
//                    val text = "disableService!"
//                    val duration = Toast.LENGTH_SHORT
//                    val toast = Toast.makeText(requireContext(), text, duration) // in Activity
//                    toast.show()
                }
                "Cài đặt" -> {
                    val settingFragment = SettingFragment()
                    fragmentTransaction.replace(R.id.fragmentView, settingFragment)
//                    val enableServiceIntent = Intent(requireContext(), NotificationService::class.java)
//                    enableServiceIntent.action = "ENABLE_SERVICE"
//                    requireContext().startService(enableServiceIntent)
//                    Log.d("disableService", "${noti.disableService()}")
//                    val text = "ENABLE_SERVICE"
//                    val duration = Toast.LENGTH_SHORT
//
//                    val toast = Toast.makeText(requireContext(), text, duration) // in Activity
//                    toast.show()

                }
            }
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }
}
