package cn.blogss.android_study.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;


import androidx.core.app.NotificationCompat;

import cn.blogss.android_study.R;

/**
 * @创建人 560266
 * @文件描述
 * @创建时间 2020/3/31
 */
public class MyService extends Service {
    private static final String TAG = "MyService";
    // 第4步，实例化一个MyBinder对象
    private MyBinder mBinder = new MyBinder(this);

    private final static String CHANNEL_ID = "cn.blogss.android_study.n1";

    private final static String CHANNEL_NAME = "n1";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"onCreate");
        NotificationChannel notificationChannel = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){/*Android 8.0及其以上*/
            notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        Intent notificationIntent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);
        Notification notification = getNotification(pendingIntent);

        startForeground(1, notification);//让Service变成前台Service,并在系统的状态栏显示出来
    }

    private Notification getNotification(PendingIntent pendingIntent) {
        //新建Builer对象
        return new NotificationCompat.Builder(this,CHANNEL_ID).
        setContentTitle(getApplicationContext().getResources().getString(R.string.service_content_title)).//设置通知的标题
        setContentText(getApplicationContext().getResources().getString(R.string.service_content)).//设置通知的内容
        setWhen(System.currentTimeMillis()).
        setSmallIcon(R.mipmap.ic_launcher).//设置通知的图标
        setContentIntent(pendingIntent).build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"onBind");
        return mBinder;//第4步，返回这个mBinder对象
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG,"onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG,"onRebind");
        super.onRebind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestroy");
        super.onDestroy();
    }


    public void receiveMsgFromnAct(String str){
        Log.d(TAG, "receive msg from activity: " + str);
    }

    /**
     *  Activity 与 Service 之间通讯的桥梁
     * */
    public static class MyBinder extends Binder{
        private MyService mService;
        private ReplyActMsgListener mListener;

        public MyBinder(MyService service) {
            this.mService = service;
        }

        public void receiveMsgFromnAct(String str) {
            // Activity通过Binder来调用Service的方法将消息传给Service
            mService.receiveMsgFromnAct(str);
            // 并回调mListener.replyActMsg告诉Activity已收到消息
            mListener.replyActMsg("hi, activity.");
        }

        // MyBinder 里面提供一个注册回调的方法
        public void setReplyActMsgListener(ReplyActMsgListener listener) {
            this.mListener = listener;
        }

        //自定义一个回调接口
        public interface ReplyActMsgListener {
            void replyActMsg(String str);
        }
    }
}
