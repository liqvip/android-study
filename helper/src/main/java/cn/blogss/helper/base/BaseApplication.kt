package cn.blogss.helper.base

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.Choreographer
import cn.blogss.core.base.CrashHandler

open class BaseApplication : Application() {
    private val frameCallback = FrameCallback()

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

        // UI 卡顿检测
        Choreographer.getInstance().postFrameCallback(frameCallback)
    }

    class FrameCallback: Choreographer.FrameCallback {
        private var lastTime = 0L
        override fun doFrame(frameTimeNanos: Long) {
            if(lastTime == 0L) {
                return
            } else {
                // 计算UI 绘制间隔
                val interval = (frameTimeNanos - lastTime)/1000000
                // 计算丢帧数
                val loseFrames = interval / 16
                if(interval > 16) { // UI 绘制间隔超过 16ms, 认为 UI 线程操作超时
                    Log.i(TAG, "UI 线程超时: ${interval}ms, 丢帧 $loseFrames")
                }
            }
            lastTime = frameTimeNanos
            Choreographer.getInstance().postFrameCallback(this)
        }
    }
}