package cn.blogss.android_study.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * @创建人 560266
 * @文件描述
 * @创建时间 2020/3/31
 */
public class MyService extends Service {
    private static final String TAG = "MyService";
    // 第4步，实例化一个MyBinder对象
    private MyBinder mBinder = new MyBinder(this);

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"onCreate");
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
