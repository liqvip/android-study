## Service 的工作过程
Service 分为两种工作状态，一种是启动状态，主要用于执行后台计算；另一种是绑定状态，主要用于其他组件和 Service 
的交互。需要注意的是，Service 的这两种状态是可以共存的，即 Service 即可以处于启动状态也可以同时处于绑定状态。
通过 Context 的 startService 方法即可启动一个 Service，如下所示。

```java
Intent intentService = new Intent(this,MyService.class);
startService(intentService);
```
通过 Context 的 bindService 方法即可以绑定的方式启动一个 Service，如下所示。

```java
Intent intentService = new Intent(this,MyService.class);
startService(intentService,mServiceConnection,BIND_AUTO_CRATE);
```

### Service 的启动过程

![Service 的启动过程](http://img.blogss.cn/myBlog/20200911170011247.jpg)

Service 的启动从 ContextWrapper 中的 startService 方法开始。mBase 的类型是 ContextImpl，Activity 被创建时
会通过 attach 方法将一个 ContextImpl 关联起来，这个 ContextImpl 对象就是 mBase。

<div align="center">ContextWrapper#startService</div>

```java
@Override
public ComponentName startService(Intent service) {
    return mBase.startService(service); // mBase 的类型是 ContextImpl
}
```

下面继续看 ContextImpl 的 startService 实现，startService 会调用 startServiceCommon 方法。

<div align="center">ContextImpl#startService,startServiceCommon</div>

```java
@Override
public ComponentName startService(Intent service) {
    warnIfCallingFromSystemProcess();
    return startServiceCommon(service, false, mUser);
}

private ComponentName startServiceCommon(Intent service, boolean requireForeground,
        UserHandle user) {
    try {
        validateServiceIntent(service);
        service.prepareToLeaveProcess(this);
        ComponentName cn = ActivityManager.getService().startService(
            mMainThread.getApplicationThread(), service, service.resolveTypeIfNeeded(
                        getContentResolver()), requireForeground,
                        getOpPackageName(), user.getIdentifier());
        return cn;
    }
}
```

ActivityManager.getService() 这个对象实际上就是 AMS(ActivityManagerService)，所以继续看 AMS 中的 startService 方法。
mServices 的类型是 ActiveServices，这个类是辅助 AMS 进行 Service 管理的类，包括 Service 的启动、绑定和停止等。

<div align="center">ActivityManagerService#startService</div>

```java
@Override
public ComponentName startService(IApplicationThread caller, Intent service,
        String resolvedType, boolean requireForeground, String callingPackage, int userId)
        throws TransactionTooLargeException {
    synchronized(this) {
        final int callingPid = Binder.getCallingPid();
        final int callingUid = Binder.getCallingUid();
        final long origId = Binder.clearCallingIdentity();
        ComponentName res;
        try {
            res = mServices.startServiceLocked(caller, service,
                    resolvedType, callingPid, callingUid,
                    requireForeground, callingPackage, userId);// mServices 的类型是 ActiveServices
        } finally {
            Binder.restoreCallingIdentity(origId);
        }
        return res;
    }
}
```

在 ActiveServices 中 startServiceLocked 方法的尾部会调用 startServiceInnerLocked，startServiceInnerLocked
方法并没有完成具体的启动工作，而是把后续的工作交给了bringUpServiceLocked 方法来处理，在bringUpServiceLocked
方法中又调用了 realStartServiceLocked 方法，这个方法真正的启动了一个 Service，它的核心代码实现如下。

<div align="center">ActiveServices#realStartServiceLocked</div>

```java
try {
    app.thread.scheduleCreateService(r, r.serviceInfo,
            mAm.compatibilityInfoForPackage(r.serviceInfo.applicationInfo),
            app.getReportedProcState());
    created = true;
}
sendServiceArgsLocked(r, execInFg, true); // 内部会调用 Service 的其他方法，比如 onStartCommand。
```

app.thread 的类型是 ApplicationThread，是 ActivityThread 的一个内部类，它继承了 IApplicationThread.Stub，
说明这里使用了 AIDL 技术实现进程间通信，Stub是 IApplicationThread 的内部类，继承了 Binder 类。
**因此，ApplicationThread 是一个 Binder**，调用 scheduleCreateService 就是进程间通信的过程。
现在流程到了 ApplicationThread 中的 scheduleCreateService 方法。

<div align="center">ApplicationThread#scheduleCreateService</div>

```java
public final void scheduleCreateService(IBinder token,
        ServiceInfo info, CompatibilityInfo compatInfo, int processState) {
    updateProcessState(processState, false);
    CreateServiceData s = new CreateServiceData();
    s.token = token;
    s.info = info;
    s.compatInfo = compatInfo;

    sendMessage(H.CREATE_SERVICE, s);
}
```

H 是 ApplicationThread 的一个内部类，继承了 Handler，所以在 handleMessage 方法中找到对 CREATE_SERVICE 消息类型的
处理，它调用了 handleCreateService 方法，如下。

<div align="center">ActivityThread#handleCreateService</div>

```java
private void handleCreateService(CreateServiceData data) {
    LoadedApk packageInfo = getPackageInfoNoCheck(
            data.info.applicationInfo, data.compatInfo);
    Service service = null;
    try {
        java.lang.ClassLoader cl = packageInfo.getClassLoader();
        service = packageInfo.getAppFactory()
                .instantiateService(cl, data.info.name, data.intent);
    }

    try {
        if (localLOGV) Slog.v(TAG, "Creating service " + data.info.name);

        ContextImpl context = ContextImpl.createAppContext(this, packageInfo);
        context.setOuterContext(service);

        Application app = packageInfo.makeApplication(false, mInstrumentation);
        service.attach(context, this, data.info.name, data.token, app,
                ActivityManager.getService());
        service.onCreate();
        mServices.put(data.token, service);
        try {
            ActivityManager.getService().serviceDoneExecuting(
                    data.token, SERVICE_DONE_EXECUTING_ANON, 0, 0);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}
```

handleCreateService 主要完成了如下几件事。
1. 首先通过类加载器创建 Service 实例。
2. 然后创建 ContextImpl 对象并通过 Service 的 attach 方法建立二者之间的关系，这个过程和 Activity 实际上是类似的，
毕竟 Service 和 Activity 都是一个 Context。
3. 接着创建 Application 对象并调用其 onCreate ,当然 Application 的创建过程只会有一次。
4. **最后调用 Service 的 onCreate** 方法并将 Service 对象存储到 ActivityThread 的一个列表中。这个列表的定义如下所示。
```java
final ArrayMap<IBinder, Service> mServices = new ArrayMap<>();
```

由于 Service 的 onCreate 方法被执行了，这也意味着 Service 已经启动了，**随后会调用 Service 的其他方法，比如 onStartCommand**。

### Service 的绑定过程

![Service 的绑定过程](http://img.blogss.cn/myBlog/20200911170002333.jpg)

Service 的绑定过程大致和启动过程一样，首先在 ContextWrapper 中调用 bindService，然后在 ContextImpl 中调用 
bindServiceCommon 方法，如下所示。

<div align="center">ContextImpl#bindServiceCommon</div>

```java
private boolean bindServiceCommon(Intent service, ServiceConnection conn, int flags,
        String instanceName, Handler handler, Executor executor, UserHandle user) {
    IServiceConnection sd;
    if (mPackageInfo != null) {
        if (executor != null) {
            sd = mPackageInfo.getServiceDispatcher(conn, getOuterContext(), executor, flags);
        } else {// 将客户端的 ServiceConnection 对象转化为 ServiceDispatcher.InnerConnection对象
            sd = mPackageInfo.getServiceDispatcher(conn, getOuterContext(), handler, flags);
        }
    }
    validateServiceIntent(service);
    try {
        IBinder token = getActivityToken();
        if (token == null && (flags&BIND_AUTO_CREATE) == 0 && mPackageInfo != null
                && mPackageInfo.getApplicationInfo().targetSdkVersion
                < android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            flags |= BIND_WAIVE_PRIORITY;
        }
        service.prepareToLeaveProcess(this);
        int res = ActivityManager.getService().bindIsolatedService(
            mMainThread.getApplicationThread(), getActivityToken(), service,
            service.resolveTypeIfNeeded(getContentResolver()),
            sd, flags, instanceName, getOpPackageName(), user.getIdentifier());
        return res != 0;
    } catch (RemoteException e) {
        throw e.rethrowFromSystemServer();
    }
}
```

bindServiceCommon 方法主要完成如下两件事情。
1. 首先将客户端的 ServiceConnection 对象转化为 ServiceDispatcher.InnerConnection 对象。之所以不能直接使用
ServiceConnection 对象，这是因为服务的绑定有可能是跨进程的，因此 ServiceConnection 对象必须借助于 Binder 
才能让远程服务端回调自己的方法。而 ServiceDispatcher 的内部类 InnerConnection 刚好充当了 Binder 这个角色。
当 Service 和客户端建立联系后，系统会通过 InnerConnection 来调用 ServiceConnection 中的 onServiceConnected 方法。
2. 接着 bindServiceCommon 方法会通过 AMS 来完成 Service 的具体绑定过程，这对应 AMS 的 bindIsolatedService 方法。

<div align="center">ActivityManagerService#bindIsolatedService</div>

```java
public int bindIsolatedService(IApplicationThread caller, IBinder token, Intent service,
        String resolvedType, IServiceConnection connection, int flags, String instanceName,
        String callingPackage, int userId) throws TransactionTooLargeException {

    synchronized(this) {
        return mServices.bindServiceLocked(caller, token, service,
                resolvedType, connection, flags, instanceName, callingPackage, userId);
    }
}
```

AMS 中的 bindIsolatedService 方法会调用 ActiveServices 的 bindServiceLocked 方法，bindServiceLocked 再调用
requestServiceBindingLocked 方法。对于 r.app.thread 我们再也熟悉不过，它就是 ApplicationThread 对象，那么最后
的处理还是会交给 ApplicationThread 的内部类 H 来处理，这里就不再详细说明。

<div align="center">ActiveServices#requestServiceBindingLocked</div>

```java
private final boolean requestServiceBindingLocked(ServiceRecord r, IntentBindRecord i,
        boolean execInFg, boolean rebind) throws TransactionTooLargeException {
    if ((!i.requested || rebind) && i.apps.size() > 0) {
        try {
            bumpServiceExecutingLocked(r, execInFg, "bind");
            r.app.forceProcessStateUpTo(ActivityManager.PROCESS_STATE_SERVICE);
            r.app.thread.scheduleBindService(r, i.intent.getIntent(), rebind,
                    r.app.getReportedProcState());
            if (!rebind) {
                i.requested = true;
            }
            i.hasBound = true;
            i.doRebind = false;
        }
    }
    return true;
}
```

通过 scheduleBindService 方法发送的是一个 BIND_SERVICE 类型的消息，H 对这个消息类型的处理如下，如果是第一次
绑定，那么首先会调用 Service 的 onBind 方法，然后调用 AMS 中的 publishService 方法。如果是多次绑定同一个 Service，
那么首先调用 Service 的 onRebind 方法，然后调用 AMS 中的 serviceDoneExecuting 方法。

<div align="center">ActivityThread#handleBindService</div>

```java
private void handleBindService(BindServiceData data) {
    Service s = mServices.get(data.token);
    if (DEBUG_SERVICE)
        Slog.v(TAG, "handleBindService s=" + s + " rebind=" + data.rebind);
    if (s != null) {
        try {
            data.intent.setExtrasClassLoader(s.getClassLoader());
            data.intent.prepareToEnterProcess();
            try {
                if (!data.rebind) {// 第一次绑定
                    IBinder binder = s.onBind(data.intent);
                    ActivityManager.getService().publishService(
                            data.token, data.intent, binder);
                } else {// 多次绑定同一个 Service
                    s.onRebind(data.intent);
                    ActivityManager.getService().serviceDoneExecuting(
                            data.token, SERVICE_DONE_EXECUTING_ANON, 0, 0);
                }
            } catch (RemoteException ex) {
                throw ex.rethrowFromSystemServer();
            }
        }
    }
}
```

publishService 尾部调用了 ActiveServices 中的 publishServiceLocked ，这个方法看起来很复杂，但其实核心代码只有
就只有一句话：c.conn.connected(r.name, service, false)，其中 c 的类型是 ConnectionRecord，c.conn 的类型是
LoadedApk.ServiceDispatcher.InnerConnection，service 就是 Service 的 onBind 方法返回的 Binder 对象。为了分析
具体的逻辑，下面看一下 InnerConnection 的 connected 方法。


<div align="center"><b>InnerConnection#connected</b></div>

```java
public void connected(ComponentName name, IBinder service, boolean dead)
        throws RemoteException {
    LoadedApk.ServiceDispatcher sd = mDispatcher.get();
    if (sd != null) {
        sd.connected(name, service, dead);
    }
}
```

connected 方法又调用了 ServiceDispatcher的 connected 方法。mActivityThread 就是 ActivityThread 中的 H，
这样一来，RunConnection 就可以经由 H 的 post 方法从而运行在主线程中，因此客户端 ServiceConnection 中的方法
是在主线程被回调的。

<div align="center"><b>ServiceDispatcher的#connected</b></div>

```java
public void connected(ComponentName name, IBinder service, boolean dead) {
    if (mActivityExecutor != null) {
        mActivityExecutor.execute(new RunConnection(name, service, 0, dead));
    } else if (mActivityThread != null) {// 调用 
        mActivityThread.post(new RunConnection(name, service, 0, dead));
    } else {
        doConnected(name, service, dead);
    }
}
```

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