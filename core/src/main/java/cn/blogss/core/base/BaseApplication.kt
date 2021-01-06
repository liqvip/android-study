package cn.blogss.core.base

import android.app.Application

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
        CrashHandler.getInstance().init(this)
    }
}