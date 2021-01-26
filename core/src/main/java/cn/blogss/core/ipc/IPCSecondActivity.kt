package cn.blogss.core.ipc

import android.os.Bundle
import android.util.Log
import android.view.View
import cn.blogss.core.R
import cn.blogss.core.base.BaseActivity

class IPCSecondActivity : BaseActivity(), View.OnClickListener {
    companion object {
        private const val TAG = "IPCSecondActivity"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_ipc_second
    }

    override fun initView() {
        Log.d(TAG, "sUserId: "+User.sUserID)    // sUserId: 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTag(TAG)
        super.onCreate(savedInstanceState)
    }

    override fun onClick(v: View?) {

    }
}