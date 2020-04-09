package cn.blogss.core.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import cn.blogss.core.R;

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

    private final static String CHANNEL_NAME = "聊天消息";

    NotificationManager notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"onCreate");
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){/*确保的是当前手机的系统版本必须是Android 8.0系统或者更高，
        因为低版本的手机系统并没有通知渠道这个功能，不做系统版本检查的话会在低版本手机上造成崩溃。*/
            createNotificationChannel();
        }
        Notification notification = getNotification();
        notificationManager.notify(1,notification);
        //startForeground(1, notification);//让Service变成前台Service,并在系统的状态栏显示出来
    }

    /*创建一个通知渠道至少需要渠道ID、渠道名称以及重要等级这三个参数，其中渠道ID可以随便定义，只要保证全局唯一性就可以。
        渠道名称是给用户看的，需要能够表达清楚这个渠道的用途。重要等级的不同则会决定通知的不同行为，
        当然这里只是初始状态下的重要等级，用户可以随时手动更改某个渠道的重要等级，App是无法干预的
    */
    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(notificationChannel);
    }


    private Notification getNotification() {
        Intent notificationIntent = new Intent(this, ServiceActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);
        //新建Builer对象
        return new NotificationCompat.Builder(this,CHANNEL_ID).
        setContentTitle(getApplicationContext().getResources().getString(R.string.service_content_title)).//设置通知的标题
        setContentText(getApplicationContext().getResources().getString(R.string.service_content)).//设置通知的内容
        setWhen(System.currentTimeMillis()).
        setSmallIcon(R.mipmap.ic_launcher).//设置通知的图标
        setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher)).
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
