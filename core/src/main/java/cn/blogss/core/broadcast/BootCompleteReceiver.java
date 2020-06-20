package cn.blogss.core.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * 静态注册注册的一个广播接收器，可以监听系统开机
 */
public class BootCompleteReceiver extends BroadcastReceiver {
    private static final String TAG = "BootCompleteReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Boot Completed.");
        Toast.makeText(context, "Boot Completed.", Toast.LENGTH_LONG).show();
    }
}
