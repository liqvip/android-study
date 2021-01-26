package cn.blogss.core.ipc

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import cn.blogss.core.R
import cn.blogss.core.base.BaseActivity

class IPCFirstActivity : BaseActivity(), View.OnClickListener {
    private lateinit var btGo: AppCompatButton

    companion object {
        private const val TAG = "IPCFirstActivity"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_ipc_first
    }

    override fun initView() {
        btGo = findViewById(R.id.bt_go_second)

        Log.d(TAG, "sUserId: "+User.sUserID)
        User.sUserID++
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTag(TAG)
        super.onCreate(savedInstanceState)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.bt_go_second -> {
                startActivity(Intent(this,IPCSecondActivity::class.java))
            }
        }
    }
}