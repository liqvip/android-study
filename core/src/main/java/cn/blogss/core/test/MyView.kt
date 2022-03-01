package cn.blogss.core.test

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

/**
 * @author: Little Bei
 * @Date: 2022/2/16
 */
class MyView(context: Context, attributeSet: AttributeSet): View(context, attributeSet) {
    companion object {
        private const val TAG = "MyView"
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        var action = ""
        when(ev.action) {
            MotionEvent.ACTION_DOWN -> {
                action = "ACTION_DOWN"
            }
            MotionEvent.ACTION_MOVE -> {
                action = "ACTION_MOVE"
            }
            MotionEvent.ACTION_UP -> {
                action = "ACTION_UP"
            }
            MotionEvent.ACTION_CANCEL -> {
                action = "ACTION_CANCEL"
            }
        }
        Log.d(TAG, "dispatchTouchEvent: action = $action")
        val consume =  if (super.dispatchTouchEvent(ev)) "true" else "false"
//        Log.d(TAG, "dispatchTouchEvent: intercept = $consume")
        return consume == "true"
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var action = ""
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                action = "ACTION_DOWN"
            }
            MotionEvent.ACTION_MOVE -> {
                action = "ACTION_MOVE"
            }
            MotionEvent.ACTION_UP -> {
                action = "ACTION_UP"
            }
            MotionEvent.ACTION_CANCEL -> {
                action = "ACTION_CANCEL"
            }
        }
        Log.d(TAG, "onTouchEvent: action = $action")
//        val consume = if (super.onTouchEvent(event)) "true" else "false"
//        Log.d(TAG, "onTouchEvent: intercept = $consume")
//        return consume == "true"
        return true
    }


}