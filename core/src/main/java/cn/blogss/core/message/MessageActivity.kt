package cn.blogss.core.message

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import cn.blogss.core.R
import cn.blogss.helper.base.BaseActivity

class MessageActivity : BaseActivity() {
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

    override fun getLayoutId(): Int {
        return R.layout.activity_message
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

    override fun onCreate(savedInstanceState: Bundle?) {
        setTag(TAG)
        super.onCreate(savedInstanceState)
    }
}