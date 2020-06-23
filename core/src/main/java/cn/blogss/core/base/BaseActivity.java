package cn.blogss.core.base;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import cn.blogss.core.R;
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
        contentNotOverlayStatusBar();
        initView();
    }

    /**
     * 设置布局内容不与状态栏重叠
     */
    private void contentNotOverlayStatusBar() {
        View decorView = getWindow().getDecorView();
        View contentView = ((ViewGroup) decorView.findViewById(android.R.id.content)).getChildAt(0);
        int statusBarHeight = getStatusBarHeight(this);
        contentView.setPadding(0,statusBarHeight,0,0);
    }

    /**
     *  隐藏 ActionBar
     * */
    private void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.hide();
    }

    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    public int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
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
