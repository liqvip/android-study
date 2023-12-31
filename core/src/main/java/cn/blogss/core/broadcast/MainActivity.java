package cn.blogss.core.broadcast;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import cn.blogss.core.R;
import cn.blogss.helper.base.BaseActivity;

/**
 * 广播接收器，动态注册与静态注册,接收系统广播
 * 示例:
 * 1.动态注册一个广播，监听系统网络变化
 * 2.静态注册一个广播，发送一个自定义的广播
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "broadcast:MainActivity";

    private IntentFilter intentFilter;

    private NetChangeReceiver netChangeReceiver;

    /**==================本地广播部分，Android 新版本中已被废弃======================**/


    private Button btSendBroadcastUnused, btSendBroadcast, btSendOfflineBroadcast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intentFilter = new IntentFilter();
        /*【示例1】网络状态发生变化时，系统会发送这样一条广播*/
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        netChangeReceiver = new NetChangeReceiver();
        /*注册广播接收器*/
        registerReceiver(netChangeReceiver,intentFilter);

        /*本地广播部分*/
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_broadcast;
    }

    @Override
    protected void initView() {
        btSendBroadcastUnused = findViewById(R.id.bt_send_broadcast_unused);
        btSendBroadcast = findViewById(R.id.bt_send_broadcast);
        btSendOfflineBroadcast = findViewById(R.id.bt_send_offline_broadcast);

        btSendBroadcastUnused.setOnClickListener(this);
        btSendBroadcast.setOnClickListener(this);
        btSendOfflineBroadcast.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*Act 生命周期结束后，取消注册*/
        unregisterReceiver(netChangeReceiver);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        /*
         *【示例2】发送一个自定义的广播，所有监听这条广播的广播接收器都会收到消息。此时发出去的广播就是一条标准广播
         *由于广播是使用Intent进行传递的，因此还可以在Intent中携带一些数据传递给广播接收器
         */
        Intent intent;
        if(id == R.id.bt_send_broadcast_unused){
            intent = new Intent("cn.blogss.core.broadcast.MY_BROADCAST");
            sendBroadcast(intent);
        }else if(id == R.id.bt_send_broadcast){
            intent = new Intent("cn.blogss.core.broadcast.MY_BROADCAST");
            intent.setComponent(new ComponentName(this, "cn.blogss.core.broadcast.MyBroadcastReceiver"));
            sendBroadcast(intent);
            //sendOrderedBroadcast(intent, null);
        }else if(id == R.id.bt_send_offline_broadcast){/*发送一条强制下线的广播*/
            intent = new Intent("cn.blogss.core.base.FORCE_OFFLINE");
            sendBroadcast(intent);
        }
    }

    /**
     * 创建一个广播接收器，只需要让它继承自 BroadcastReceiver，重写父类的onReceive方法。
     * 这样当有广播到来时，这个方法就可以得到执行
     */
    class NetChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            /*不要在onReceive()方法中添加过多的逻辑或进行任何的耗时操作，因为在广播接收器中是不允许开启线程的*/
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

    /**
     * 本地广播接收器
     */
    class LocalReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "received local broadcast", Toast.LENGTH_SHORT).show();
        }
    }
}