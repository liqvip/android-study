package cn.blogss.core.remoteviews

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.widget.RemoteViews
import cn.blogss.core.R
import java.util.*

/**
 * @创建人 560266
 * @文件描述 小部件的实现类
 * @创建时间 2020/7/31
 */
class MyAppWidgetProvider: AppWidgetProvider() {
    companion object {
        const val TAG: String = "MyAppWidgetProvider"
        const val CLICK_ACTION: String = "cn.blogss.core.remoteviews.CLICK"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        Log.i(TAG, "onReceive : action = " + intent!!.action)
        /*这里判断是自己的 action, 做自己的事情，比如小部件被单击了要干什么，这里是做一个动画效果*/
        val action = intent.action
        if(action == AppWidgetManager.ACTION_APPWIDGET_UPDATE){
            val handler = Handler()
            val rotateTask = object: Runnable {
                override fun run() {
                    val srcbBitmap: Bitmap = BitmapFactory.decodeResource(context!!.resources, R.drawable.dog)
                    val appWidgetManager: AppWidgetManager = AppWidgetManager.getInstance(context)
                    for (i in 0 until 37){
                        val degree: Float = (i * 10.0f) % 360
                        val remoteViews = RemoteViews(context.packageName, R.layout.widget)
                        remoteViews.setImageViewBitmap(R.id.iv_widget, rotateBitmap(context, srcbBitmap, degree))
                        /*val intentClick = Intent(CLICK_ACTION)
                        val pendingIntent = PendingIntent.getBroadcast(context, 0, intentClick, 0)
                        remoteViews.setOnClickPendingIntent(R.id.iv_widget, pendingIntent)*/
                        appWidgetManager.updateAppWidget(ComponentName(context, MyAppWidgetProvider::class.java), remoteViews)
                    }
                    handler.postDelayed(this, 5000)
                }
            }
            handler.postDelayed(rotateTask, 5000)
        }
    }

    /*每次桌面小部件更新时都调用一次该方法*/
    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        Log.i(TAG, "onUpdate")

        val counter = appWidgetIds!!.size
        Log.i(TAG, "counter = $counter")
        for (i in 0 until counter){
            val appWidgetId = appWidgetIds[i]
            onWidgetUpdate(context, appWidgetManager, appWidgetId)
        }
    }

    /*桌面小部件更新*/
    private fun onWidgetUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetId: Int) {
        Log.i(TAG, "appWidgetId = $appWidgetId")
        val remoteViews = RemoteViews(context!!.packageName, R.layout.widget)

        /*单击"桌面小部件"发送的广播*/
        /*val intentClick = Intent(CLICK_ACTION)
        val pendingIntent = PendingIntent.getBroadcast(context, 0 ,intentClick, 0)
        remoteViews.setOnClickPendingIntent(R.id.iv_widget, pendingIntent)*/
        appWidgetManager!!.updateAppWidget(appWidgetId, remoteViews)
    }

    private fun rotateBitmap(context: Context, srcbBitmap: Bitmap, degree: Float): Bitmap? {
        val matrix = Matrix()
        matrix.reset()
        matrix.setRotate(degree)
        return Bitmap.createBitmap(srcbBitmap, 0 , 0, srcbBitmap.width, srcbBitmap.height, matrix, true)
    }
}