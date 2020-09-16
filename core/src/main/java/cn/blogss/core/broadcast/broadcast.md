## 广播接收器 BroadcastReceiver
广播接收器更多的是扮演一种打开程序其他组件的角色。比如创建一条状态栏通知，或者启动一个服务等。广播是一种可以跨进程通信的方式。
因此我们在应用程序内发出的广播，其他的应用程序应该也是可以收到的。

## BroadcastReceiver 的工作过程
### 广播的注册过程
广播的注册分为静态注册和动态注册，其中静态注册的广播在应用安装时由系统自动完成注册，具体来说是由 PMS(PackageManagerService)
来完成整个注册过程的，除了广播以外，其他三大组件也都是在应用安装时由 PMS 解析并注册的。这里只分析广播的动态注册的过程，动态
注册的过程是从 ContextWrapper 的 registerReceiver 方法开始的，和 Activity 以及 Service 一样。ContextWrapper 并没有做实际的
工作，而是将注册过程直接交给了 ContextImpl，如下所示。

<div align="center">ContextImpl#registerReceiver</div>

```java
@Override
public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
    return registerReceiver(receiver, filter, null, null);
}
```

registerReceiver 有多个重载方法，最后内部会调用 registerReceiverInternal 方法，如下所示。

<div align="center">ContextImpl#registerReceiverInternal</div>

```java
private Intent registerReceiverInternal(BroadcastReceiver receiver, int userId,
        IntentFilter filter, String broadcastPermission,
        Handler scheduler, Context context, int flags) {
    IIntentReceiver rd = null;
    if (receiver != null) {
        if (mPackageInfo != null && context != null) {
            if (scheduler == null) {
                scheduler = mMainThread.getHandler();
            }
            rd = mPackageInfo.getReceiverDispatcher(
                receiver, context, scheduler,
                mMainThread.getInstrumentation(), true);
        } else {
            if (scheduler == null) {
                scheduler = mMainThread.getHandler();
            }
            rd = new LoadedApk.ReceiverDispatcher(
                    receiver, context, scheduler, null, true).getIIntentReceiver();
        }
    }
    try {
        final Intent intent = ActivityManager.getService().registerReceiver(
                mMainThread.getApplicationThread(), mBasePackageName, rd, filter,
                broadcastPermission, userId, flags);
        if (intent != null) {
            intent.setExtrasClassLoader(getClassLoader());
            intent.prepareToEnterProcess();
        }
        return intent;
    }
}
```

在上面的代码中，系统首先从 mPackageInfo 获取 IIntentReceiver 对象，然后再采用跨进程的方式向 AMS 发送广播注册的请求。
为什么是传递 IIntentReceiver 对象而不是直接传递 BroadcastReceiver？这是因为 BroadcastReceiver 作为 Android 的一个组件
是不能直接跨进程传递的，所以需要通过 IIntentReceiver 包装一下，毫无疑问 IIntentReceiver 是一个 Binder，它的真正实现类
是LoadedApk.ReceiverDispatcher.InnerReceiver。可以发现，BroadcastReceiver 的这个过程和 Service 的实现原理类似，Service
也有一个叫 ServiceDispatcher 的类，并且其内部类 InnerConnection 也是一个 Binder 接口，作用同样是为了进程间通信。<br>

由于注册广播的真正实现过程是在 AMS 中，因此我们需要看一下 AMS 的具体实现。AMS 的 registerReceiver 方法看起来很长，其实关键点
就只有下面一部分，最终会把远程的 InnerReceiver 对象以及 IntentFilter 对象存储起来，这样整个广播的注册过程就完成了，代码如下所示。

<div align="center">ActivityManagerService#registerReceiver</div>

```java
public Intent registerReceiver(IApplicationThread caller, String callerPackage,
        IIntentReceiver receiver, IntentFilter filter, String permission, int userId,
        int flags) {
    mRegisteredReceivers.put(receiver.asBinder(), rl);
    BroadcastFilter bf = new BroadcastFilter(filter, rl, callerPackage,
            permission, callingUid, userId, instantApp, visibleToInstantApps);
    rl.add(bf);
    if (!bf.debugCheck()) {
        Slog.w(TAG, "==> For Dynamic broadcast");
    }
    mReceiverResolver.addFilter(bf);
}
```

### 广播的发送和接收过程

## 系统广播
### 静态注册
静态注册的广播接收器可以在程序未启动的的情况下就接收到广播，**Android 8 对静态注册的广播接收器做了限制**，具体限制规则如下。<br>
1.若targetSdkVersion >= 26，直接隐式发送广播，将不能收到广播<br>
2.若targetSdkVersion >= 26，指定应用程序的包名和广播接收器的类名再发送，可以收到广播。<br>
3.若targetSdkVersion <= 25，则无限制
### 动态注册
动态注册的广播接收器可以自由的控制注册与注销，灵活性很大，但是也有缺点，即必须在程序启动之后才能接收到广播。

## 自定义广播
### 标准广播
### 有序广播
发送有序广播只要改动一行代码，即将`sendBroadcast(intent)`改成`sendOrderedBroadcast(intent, null)`

## 本地广播
为了能够简单的解决广播安全性的问题，Android引入了一套本地广播机制，使用这个机制发出的广播只能够在应用程序的内部进行传递，
并且广播接收器也只能接收来自本应用程序发出的广播。本地广播是无法通过静态注册的方式来接收的。