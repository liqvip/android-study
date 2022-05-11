package cn.blogss.core.test

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import androidx.lifecycle.ViewModel
import cn.blogss.core.databinding.ActivityTestBinding
import cn.blogss.helper.base.jetpack.BaseActivity
import cn.blogss.helper.base.jetpack.BaseViewModel

/**
 * @author: Little Bei
 * @Date: 2022/2/16
 */
class TestActivity: BaseActivity<ActivityTestBinding, BaseViewModel>() {

    companion object {
        private const val TAG = "TestActivity"
    }

    override fun getViewModel(): BaseViewModel? {
        return null
    }

    override fun getViewBinding(inflater: LayoutInflater): ActivityTestBinding {
        return ActivityTestBinding.inflate(inflater)
    }

    override fun initView() {
    }

    override fun bindObserver() {
    }

    override fun initData() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: one params, savedInstanceState = $savedInstanceState")
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        Log.i(TAG, "onCreate: savedInstanceState = $savedInstanceState, persistentState = $persistentState")
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy: ")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState: outState = $outState")
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        Log.i(TAG, "onSaveInstanceState: outState = $outState, outPersistentState = $outPersistentState")
    }

    override fun onStateNotSaved() {
        super.onStateNotSaved()
        Log.i(TAG, "onStateNotSaved: ")
    }

    override fun onRetainCustomNonConfigurationInstance(): Any? {
        return super.onRetainCustomNonConfigurationInstance()
        Log.i(TAG, "onRetainCustomNonConfigurationInstance: ")
    }

}