package com.example.notisave.service

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log

class ProgressBarService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        // Khởi chạy CountDownTimer với thời gian là 10 giây và cập nhật ProgressBar mỗi 100ms
        object : CountDownTimer(10000, 100) {
            override fun onTick(millisUntilFinished: Long) {
                // Cập nhật giá trị ProgressBar dựa trên thời gian còn lại
                val progress = ((10000 - millisUntilFinished) / 100).toInt()
                Log.d("ProgressBarService", "Progress: $progress")
            }

            override fun onFinish() {
                // Công việc sau khi đếm ngược kết thúc (nếu cần)
                stopSelf() // Dừng dịch vụ khi đếm ngược kết thúc
            }
        }.start()
    }
}