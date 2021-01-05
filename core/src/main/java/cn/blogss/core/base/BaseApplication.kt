package cn.blogss.core.base

import android.app.Application

/**
 * @Author: Thatcher Li
 * @Date: 2021/1/5
 * @LastEditors: Thatcher Li
 * @LastEditTime: 2021/1/5
 * @Descripttion: 对 Application 的一层封装
 */
class BaseApplication private constructor() : Application() {

    companion object {
        private const val TAG = "BaseApplication"
        private lateinit var sInstance: BaseApplication

        fun getInstance(): BaseApplication{
            return sInstance
        }
    }

    override fun onCreate() {
        super.onCreate()
        sInstance = this
        CrashHandler.getInstance().init(this)
    }
}