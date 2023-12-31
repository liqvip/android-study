package cn.blogss.core.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * @创建人 560266
 * @文件描述    静态注册的一个广播接收器，用来接收自定义的广播
 * @创建时间 2020/6/22
 */
public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "received in MyBroadcastReceiver", Toast.LENGTH_SHORT).show();
        /*有序广播中，可以截断广播的传递。后续优先级低的广播接收器将无法接收到这条广播*/
        //abortBroadcast();
    }
}
