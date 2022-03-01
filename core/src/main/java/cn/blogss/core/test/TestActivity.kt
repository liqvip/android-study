package cn.blogss.core.test

import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import androidx.lifecycle.ViewModel
import cn.blogss.core.databinding.ActivityTestBinding
import cn.blogss.helper.base.jetpack.BaseActivity

/**
 * @author: Little Bei
 * @Date: 2022/2/16
 */
class TestActivity: BaseActivity<ActivityTestBinding,ViewModel>() {

    companion object {
        private const val TAG = "TestActivity"
    }

    override fun getViewModel(): ViewModel? {
        return null
    }

    override fun getViewBinding(inflater: LayoutInflater): ActivityTestBinding {
        return ActivityTestBinding.inflate(layoutInflater)
    }

    override fun initView() {
    }

    override fun bindObserver() {
    }

    override fun initData() {
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
        val consume = if (super.onTouchEvent(event)) "true" else "false"
//        Log.d(TAG, "onTouchEvent: intercept = $consume")
        return consume == "true"
    }

}