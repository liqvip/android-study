package cn.blogss.helper

import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.View
import android.view.WindowManager

/**
 * @创建人 560266
 * @文件描述 Window 窗口的 CRUD 操作
 * @创建时间 2020/8/24
 */

fun getWindowLayoutParamsInstance(x: Int, y: Int, windowType: Int): WindowManager.LayoutParams{
    val layoutParams: WindowManager.LayoutParams = WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT, windowType, 0, PixelFormat.TRANSPARENT)
    layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
    layoutParams.gravity = Gravity.START or Gravity.TOP
    layoutParams.x = x
    layoutParams.y = y
    return layoutParams
}

/**
 * 创建一个 window
 */
fun createWindow(view: View, layoutParams: WindowManager.LayoutParams, context: Context){
    val windowManager: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    windowManager.addView(view, layoutParams)
}


/**
 * 删除 window
 */
fun delWindow(view: View, context: Context){
    val windowManager: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    windowManager.removeView(view)
}

fun updateWindow(view: View, context: Context, layoutParams: WindowManager.LayoutParams){
    val windowManager: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    windowManager.updateViewLayout(view,layoutParams)
}