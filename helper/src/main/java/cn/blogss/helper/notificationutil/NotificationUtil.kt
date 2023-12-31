package cn.blogss.helper.notificationutil

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

/**
 * @创建人 560266
 * @文件描述 返回对应的通知渠道实例
 * @创建时间 2020/7/27
 */


/**
 * 创建一个通知渠道至少需要渠道ID、渠道名称以及重要等级这三个参数，其中渠道ID可以随便定义，只要保证全局唯一性就可以。
 * 渠道名称是给用户看的，需要能够表达清楚这个渠道的用途。重要等级的不同则会决定通知的不同行为，
 * 当然这里只是初始状态下的重要等级，用户可以随时手动更改某个渠道的重要等级，App是无法干预的
 */

const val CHANNEL_ID: String = "cn.blogss.android_study"

/**
 * 获取一个通知渠道
 */
@RequiresApi(Build.VERSION_CODES.O)
fun getChannel(name: String): NotificationChannel{
    return when (name) {
        ChannelName.AD.name -> {
            NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_LOW)
        }
        ChannelName.MESSAGE.name -> {
            NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH)
        }

        else -> {
            NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT)
        }
    }
}

/**
 * 获取一个通知
 */
fun getNotification(context: Context, cls: Class<*>, channelName: String, title: String, content: String, icon: Int): Notification {
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){/*确保的是当前手机的系统版本必须是Android 8.0系统或者更高，
        因为低版本的手机系统并没有通知渠道这个功能，不做系统版本检查的话会在低版本手机上造成崩溃。*/
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(getChannel(channelName))
    }
    val intent = Intent(context, cls)
    val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    val resources = context.resources
    return NotificationCompat.Builder(context, CHANNEL_ID).
    setSmallIcon(icon).
    setLargeIcon(BitmapFactory.decodeResource(resources, icon)).
    setContentTitle(title).
    setContentText(content).
    setAutoCancel(true).
    setContentIntent(pendingIntent).
    build()
}

/**
 * 获取一个自定义布局的通知
 */
fun getCustomNotification(context: Context, cls: Class<*>, channelName: String, remoteViews: RemoteViews, icon: Int): Notification {
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){/*确保的是当前手机的系统版本必须是Android 8.0系统或者更高，
        因为低版本的手机系统并没有通知渠道这个功能，不做系统版本检查的话会在低版本手机上造成崩溃。*/
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(getChannel(channelName))
    }
    val intent = Intent(context, cls)
    val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    return NotificationCompat.Builder(context, CHANNEL_ID).
    setSmallIcon(icon).
    setAutoCancel(true).
    setCustomContentView(remoteViews).
    setContentIntent(pendingIntent).
    build()
}
