package cn.blogss.core.network;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import cn.blogss.core.R;
import cn.blogss.core.base.BaseActivity;

public class NetworkActivity extends BaseActivity implements View.OnClickListener {
    private WebView wvBaiDu;


    @Override
    public int getLayoutId() {
        return R.layout.activity_network;
    }

    @Override
    protected void initView() {
        wvBaiDu = findViewById(R.id.wv_baidu);
        /*让WebView支持JavaScript脚本*/
        wvBaiDu.getSettings().setJavaScriptEnabled(true);
        wvBaiDu.setWebViewClient(new WebViewClient());
        /*访问网络需要声明权限*/
        wvBaiDu.loadUrl("http://www.baidu.com");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
    }
}