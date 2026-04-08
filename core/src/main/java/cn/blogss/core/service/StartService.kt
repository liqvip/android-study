package cn.blogss.core.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import cn.blogss.helper.LogRepository

class StartService: Service() {
    
    companion object {
        private const val TAG = "StartService"
    }

    override fun onCreate() {
        super.onCreate()
        LogRepository.addLog("$TAG, onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        LogRepository.addLog("$TAG, onStartCommand, startId = $startId")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        LogRepository.addLog("$TAG, onBind")
        return null
    }

    override fun onDestroy() {
        LogRepository.addLog("$TAG, onDestroy")
        super.onDestroy()
    }
}