package cn.blogss.core.remoteviews

import android.app.NotificationManager
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.RemoteViews
import cn.blogss.core.R
import cn.blogss.core.base.BaseActivity
import cn.blogss.helper.notificationutil.ChannelName
import cn.blogss.helper.notificationutil.getCustomNotification
import cn.blogss.helper.notificationutil.getNotification
import java.util.*

class MainActivity : BaseActivity(), View.OnClickListener {
    private lateinit var btPop: Button
    private lateinit var btCusPop: Button

    override fun getLayoutId(): Int {
        return R.layout.activity_remoteviews
    }

    override fun initView() {
        btPop = findViewById(R.id.bt_pop_one_notification)
        btCusPop = findViewById(R.id.bt_pop_one_custom_notification)

        btPop.setOnClickListener(this)
        btCusPop.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val id = v!!.id
        if(id == R.id.bt_pop_one_notification){
            popNotification()
        }else if(id == R.id.bt_pop_one_custom_notification){
            popCusNotification()
        }
    }

    private fun popNotification() {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = getNotification(this@MainActivity, MainActivity::class.java, ChannelName.DEFAULT.channelName,
                getString(R.string.AS_Notification_Title_Default), getString(R.string.AS_Notification_Content_Default), R.mipmap.ic_launcher)
        manager.notify(1, notification)
    }

    private fun popCusNotification(){
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val remoteViews = RemoteViews(packageName, R.layout.layout_custom_notification)
        remoteViews.setTextViewText(R.id.tv_notification_time, Date(System.currentTimeMillis()).toString())
        val cusNotification = getCustomNotification(this@MainActivity, MainActivity::class.java,
                ChannelName.MESSAGE.channelName, remoteViews, R.mipmap.ic_launcher)
        manager.notify(2, cusNotification)
    }
}