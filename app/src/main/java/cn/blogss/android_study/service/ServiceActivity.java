package cn.blogss.android_study.service;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import cn.blogss.android_study.R;
import cn.blogss.android_study.base.BaseActivity;

/**
 *  类关系:Activity->Binder<->Service
 * */
public class ServiceActivity extends BaseActivity implements View.OnClickListener {
    private Button btnStartSer,btnStopSer,btnBindSer,btnUnbindSer;

    private static final String TAG = "ServiceActivity";

    public MyService.MyBinder mBinder;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //第5步所说的在Activity里面取得Service里的binder对象 
            mBinder = (MyService.MyBinder)iBinder;
            //第6步注册自定义回调 
            mBinder.setReplyActMsgListener(new MyService.MyBinder.ReplyActMsgListener() {
                @Override
                public void replyActMsg(String str) {
                    Log.d(TAG, "receive msg from service: "+str);
                }
            });
            mBinder.receiveMsgFromnAct("hi, Service .");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_service;
    }

    @Override
    protected void initView() {
        btnStartSer = findViewById(R.id.bt_start_service);
        btnStopSer = findViewById(R.id.bt_stop_service);
        btnBindSer = findViewById(R.id.bt_bind_service);
        btnUnbindSer = findViewById(R.id.bt_unbind_service);

        btnStopSer.setOnClickListener(this);
        btnStartSer.setOnClickListener(this);
        btnBindSer.setOnClickListener(this);
        btnUnbindSer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            /*本地Service的启动与停止*/
            case R.id.bt_start_service:
                Intent startIntent = new Intent(this,MyService.class);
                startService(startIntent);
                break;
            case R.id.bt_stop_service:
                Intent stopIntent = new Intent(this,MyService.class);
                stopService(stopIntent);
                break;
            /*可通讯Service的绑定与解绑*/
            case R.id.bt_bind_service:
                /*第一个参数:Intent对象
                第二个参数:上面创建的Serviceconnection实例
                第三个参数:标志位
                这里传入BIND_AUTO_CREATE表示在Activity和Service建立关联后自动创建Service
                这会使得MyService中的onCreate()方法得到执行，但onStartCommand()方法不会执行*/
                Intent intent = new Intent(this,MyService.class);
                bindService(intent,mConnection,BIND_AUTO_CREATE);
                break;
            case R.id.bt_unbind_service:
                unbindService(mConnection);
                break;
        }
    }
}
