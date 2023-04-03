package cn.blogss.core.message

import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import cn.blogss.core.databinding.ActivityMessageBinding
import cn.blogss.helper.base.jetpack.BaseActivity
import cn.blogss.helper.base.jetpack.BaseViewModel

class MessageActivity : BaseActivity<ActivityMessageBinding, BaseViewModel>() {
    private val runTask = Runnable {
        Log.i(TAG, Thread.currentThread().name+" execute run method.")
    }

    private val handler = object: Handler() {
        override fun handleMessage(msg: Message) {
            Log.i(TAG, Thread.currentThread().name+" execute handleMessage method.")
        }
    }

    companion object {
        private const val TAG = "MessageActivity"
    }

    private val handlerThread = HandlerThread("message-handler-thread")
    private lateinit var myHandler: Handler
    private val myMainHandler = Handler()

    override fun getViewModel(): BaseViewModel? {
        return null
    }

    override fun getViewBinding(inflater: LayoutInflater): ActivityMessageBinding {
        return ActivityMessageBinding.inflate(inflater)
    }

    override fun initView() {
        Thread {
            val tName = Thread.currentThread().name
            Log.i(TAG, "initView: $tName sleep 1000ms then execute handler post method.")
            Thread.sleep(1000)
            // 使用 post 方法可以不用重写 handler 的 handleMessage 方法，但是无法传递数据。
            handler.post(runTask)
            Log.i(TAG, "initView: $tName sleep 1000ms then execute handler handleMessage method.")
            Thread.sleep(1000)
            val msg = Message.obtain()
            // 使用 sendMessage 方法需要重写 handler 的 handleMessage 方法，可以传递数据。
            handler.sendMessage(msg)
        }.start()
    }

    override fun bindObserver() {
    }

    override fun initData() {
        myHandler = Handler(handlerThread.looper) {
            // 子线程模拟耗时操作
            Thread.sleep(2000)
            myMainHandler.post {
                // 主线程更新 UI
            }
            true
        }

        // 在需要的地方发送消息，会发送到 myHandler 的 handleMessage 方法 中
        myHandler.sendMessage(Message.obtain())
    }

    override fun onDestroy() {
        super.onDestroy()
        // 释放 HandlerThread
        handlerThread.quitSafely()
    }
}