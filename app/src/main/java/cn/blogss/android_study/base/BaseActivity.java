package cn.blogss.android_study.base;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import cn.blogss.helper.StatusBarCompatUtil;

public abstract class BaseActivity extends AppCompatActivity {
    private static String TAG = "BaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutId = getLayoutId();
        Log.d(TAG,"layoutId:"+layoutId);
        setContentView(layoutId);
        hideActionBar();
        setStatusBarColor(Color.TRANSPARENT);
        initView();
    }

    /**
     *  隐藏 ActionBar
     * */
    private void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.hide();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     *  获取布局ID
     * */
    public abstract int getLayoutId();

    /**
     *  初始化view实例
     * */
    protected abstract void initView();

    /**
     *  子类可以重写这个方法，自定义状态栏的颜色
     * @param color
     */
    protected void setStatusBarColor(int color) {
        StatusBarCompatUtil.compat(this,color);
    }
}
