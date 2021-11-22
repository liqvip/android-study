package cn.blogss.helper.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import cn.blogss.helper.ActivityCollector;
import cn.blogss.helper.StatusBarCompatUtil;

public abstract class BaseActivity extends AppCompatActivity {
    private String TAG = "";

    /*强制用户下线的一个广播接收器*/
    private ForceOfflineReceiver forceOfflineReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        ActivityCollector.addAct(this);
        int layoutId = getLayoutId();
        setContentView(layoutId);
        hideActionBar();
        setStatusBarColor(Color.TRANSPARENT);
        contentNotOverlayStatusBar();
        initView();
    }

    protected void setTag(String tag) {
        this.TAG = tag;
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
     *  隐藏 ActionBar（标题栏）
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
        Log.i(TAG, "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
    }

    /**
     * 在onResume() 中注册，onPause() 中取消注册。保证只有栈顶的活动才能接收到强制下线这个广播
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("cn.blogss.core.base.FORCE_OFFLINE");
        forceOfflineReceiver = new ForceOfflineReceiver();
        registerReceiver(forceOfflineReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
        if(forceOfflineReceiver != null){
            unregisterReceiver(forceOfflineReceiver);
            forceOfflineReceiver = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        ActivityCollector.removeAct(this);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.i(TAG, "onSaveInstanceState: two params.");
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

    /**
     * 强制用户下线的广播接收器
     */
    private class ForceOfflineReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Warning");
            builder.setMessage("You are forced to be offline. Please try to login again.");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCollector.finishAllAct();/*销毁所有活动*/
                }
            });
            builder.show();
        }
    }
}
