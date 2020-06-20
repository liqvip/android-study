package cn.blogss.core.broadcast;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import cn.blogss.core.R;
import cn.blogss.core.base.BaseActivity;

/**
 * 广播接收器，动态注册与静态注册
 * 示例，动态注册一个广播，监听系统网络变化
 */
public class MainActivity extends BaseActivity {
    private static final String TAG = "broadcast:MainActivity";

    private IntentFilter intentFilter;

    private NetChangeReceiver netChangeReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intentFilter = new IntentFilter();
        /*网络状态发生变化时，系统会发送这样一条广播*/
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        netChangeReceiver = new NetChangeReceiver();
        /*注册广播接收器*/
        registerReceiver(netChangeReceiver,intentFilter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_blank;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*Act 生命周期结束后，取消注册*/
        unregisterReceiver(netChangeReceiver);
    }

    /**
     * 创建一个广播接收器，只需要让它继承自 BroadcastReceiver，重写父类的onReceive方法。
     * 这样当有广播到来时，这个方法就可以得到执行
     */
    class NetChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(isConnectedQ29()){
                Toast.makeText(context, "network is available", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "network is unavailable", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Android 10 版本，判断网路是否连接
     */
    private boolean isConnectedQ29(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        /*需要在 AndroidMainfest 文件中声明访问网络权限*/
        Network network = connectivityManager.getActiveNetwork();
        if(network == null)
            return false;
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
        if(networkCapabilities == null)
            return false;
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }
}