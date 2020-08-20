package cn.blogss.core.window

import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import cn.blogss.core.R
import cn.blogss.core.base.BaseActivity

class MainActivity : BaseActivity(), View.OnClickListener {
    private lateinit var btCreate : Button

    override fun getLayoutId(): Int {
        return R.layout.activity_window
    }

    override fun initView() {
        btCreate = findViewById(R.id.bt_create_one_window)

        btCreate.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.bt_create_one_window -> {
                createOneWindow()
            }
        }
    }

    /**
     * 使用 WindowManager 添加一个 Window
     */
    private fun createOneWindow() {
        val floatingButton = Button(this)
        val windowManager: WindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        floatingButton.text = "Button"
        val layoutParams: WindowManager.LayoutParams = WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION, 0, PixelFormat.TRANSPARENT)
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
        layoutParams.gravity = Gravity.START or Gravity.TOP
        layoutParams.x = 100
        layoutParams.y = 300
        windowManager.addView(floatingButton, layoutParams)
    }

}