### 1. Service 分类

#### 1.1 Service的类型
<div align="center">
<img src="https://upload-images.jianshu.io/upload_images/944365-d42fa78828930bdb.png?imageMogr2/auto-orient/strip|imageView2/2/w/339" alt="">
</div><br>

#### 1.2 特点
<div align="center">
<img src="https://upload-images.jianshu.io/upload_images/944365-8855e3a5340bece5.png?imageMogr2/auto-orient/strip|imageView2/2/w/1010" alt="">
</div><br>

#### 1.3 使用场景
<div align="center">
<img src="https://upload-images.jianshu.io/upload_images/944365-8a3cef8a174ae4b8.png?imageMogr2/auto-orient/strip|imageView2/2/w/982" alt="">
</div><br>

#### 1.4 Androidmanifest里Service的常见属性说明
属性 | 说明 | 备注
---|---|---
android:name | Service的类名 |
android:label | Service的名字 | 若不设置，默认为Service类名
android:icon  | Service的图标 |
android:permission | 申明此Service的权限 | 有提供了该权限的应用才能控制或连接此服务
android:process | 表示该服务是否在另一个进程中运行（远程服务) | 不设置默认为本地服务；remote则设置成远程服务
android:enabled | 系统默认启动 | true：Service 将会默认被系统启动；不设置则默认为false
android:exported | 该服务是否能够被其他应用程序所控制或连接 | 不设置默认此项为 false

### 2. 本地 Service
#### 2.1 生命周期
onCreate->onStartCommand->onDestroy


### 3. 可通讯 Service
#### 3.1 生命周期
onCreate->onBind->onUnbind->onDestroy

### 4. 前台 Service
#### 4.1 使用方法
1. 用法很简单，只需要在原有的Service类对onCreate()方法进行稍微修改即可。
``` java
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
```
2. 安卓8.0以后添加了前台所需要的权限<br>
`<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />`

#### 4.2 测试结果
运行后，当点击启动服务或绑定服务按钮，Service就会以前台Service的模式启动（通知栏上有通知）

### 5. 远程 Service