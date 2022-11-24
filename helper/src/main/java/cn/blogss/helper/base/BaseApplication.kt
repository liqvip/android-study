package cn.blogss.helper.base

import android.app.Application
import android.content.Context
import android.util.Log
import cn.blogss.core.base.CrashHandler

open class BaseApplication : Application() {
    companion object {
        private const val TAG = "BaseApplication"
        private lateinit var context: Context

        fun getContext(): Context{
            return context
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate")
        CrashHandler.getInstance().init(this)
        context = applicationContext
    }
}