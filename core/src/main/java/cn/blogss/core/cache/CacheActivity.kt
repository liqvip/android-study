package cn.blogss.core.cache

import android.os.Bundle
import android.os.Environment
import android.util.Log
import cn.blogss.core.R
import cn.blogss.core.base.BaseActivity

class CacheActivity : BaseActivity() {

    companion object {
        private const val TAG = "CacheActivity"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_cache
    }

    override fun initView() {
        printPath()
    }

    private fun printPath() {
        Log.i(TAG, "Environment.getDataDirectory(): "+Environment.getDataDirectory())
        Log.i(TAG, "getFilesDir().getPath(): "+filesDir.path)
        Log.i(TAG, "getFilesDir().getAbsolutePath(): "+filesDir.absolutePath)
        Log.i(TAG, "getFilesDir().getName(): "+filesDir.name)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTag(TAG)
        super.onCreate(savedInstanceState)
    }

}