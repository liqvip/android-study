package cn.blogss.core.network

import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.TextView
import cn.blogss.core.R
import cn.blogss.core.base.BaseActivity

class NetworkActivity : BaseActivity(), View.OnClickListener {
    private lateinit var wvBaiDu: WebView

    private lateinit var btSendReq: Button

    private lateinit var tvResponseContent: TextView

    override fun getLayoutId(): Int {
        return R.layout.activity_network
    }

    override fun initView() {
        wvBaiDu = findViewById(R.id.wv_baidu)
        btSendReq = findViewById(R.id.bt_send_one_req)
        tvResponseContent = findViewById(R.id.tv_response_content)

        btSendReq.setOnClickListener(this)

        /*让WebView支持JavaScript脚本*/wvBaiDu.getSettings().javaScriptEnabled = true
        wvBaiDu.webViewClient = WebViewClient()
        /*访问网络需要声明权限*/
        wvBaiDu.loadUrl("https://baidu.com")
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.bt_send_one_req -> {
                val url = "http://www.blogss.cn/"
                asynGetReq(url, object : OnRequestListener {
                    override fun onOK(result: String) {
                        runOnUiThread {/*切换到主线程更新 UI*/
                            tvResponseContent.text = result
                        }
                    }

                    override fun onFail() {
                        TODO("Not yet implemented")
                    }
                })
            }
        }
    }
}