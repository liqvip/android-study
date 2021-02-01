package cn.blogss.core.base

import android.app.Application
import android.util.Log

/**
 * @Author: Thatcher Li
 * @Date: 2021/1/5
 * @LastEditors: Thatcher Li
 * @LastEditTime: 2021/1/5
 * @Descripttion: 对 Application 的一层封装
 */
open class BaseApplication : Application() {

    companion object {
        private const val TAG = "BaseApplication"
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate")
        CrashHandler.getInstance().init(this)
    }
}