package cn.blogss.core.test

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup

/**
 * @author: Little Bei
 * @Date: 2022/2/16
 */
class InnerViewGroup(context: Context, attributeSet: AttributeSet): ViewGroup(context, attributeSet) {
    companion object {
        private const val TAG = "InnerViewGroup"
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureChildren(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (i in 0 until childCount){
            val child = getChildAt(i)
            child.layout(0, 0, child.measuredWidth, child.measuredHeight)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        var action = ""
        when(ev.action) {
            MotionEvent.ACTION_DOWN -> {
                action = "ACTION_DOWN"
            }
            MotionEvent.ACTION_MOVE -> {
                action = "ACTION_MOVE"
                Log.d(TAG, "dispatchTouchEvent: action = $action")
                return true
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
        val consume = if (super.onTouchEvent(event)) "true" else "false"
//        Log.d(TAG, "onTouchEvent: intercept = $consume")
        return consume == "true"
    }


}