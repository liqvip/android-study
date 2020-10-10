Window 表示一个窗口的概念，在日常开发中直接接触Window 的机会并不多，但是在某些特殊时候我们需要在桌面想显示一个类似悬浮窗的
东西，那么这种效果就要用到 Window 来实现。

## Window 的内部机制
Window 是一个抽象的概念，每一个 Window 都对应着一个 View 和一个 ViewRootImpl，Window 和 View 通过 ViewRootImpl 来建立联系，
因此 Window 并不是实际存在的，它是以 View 的形式存在，View 才是 Window 存在的实体。**在实际使用中无法直接访问 Window ，对 Window 
的访问必须通过 WindowManager。**

### Window 的添加过程
WindowManager 是一个接口，它的实现类是 WindowManagerImpl，WindowManagerImpl 并没有直接实现 Window 的三大操作。
而是全部交给了 WindowManagerGlobal 来处理，WindowManagerGlobal 是一个单例类。
Window 的添加过程实际是调用了 WindowManagerGlobal->addView(params) 方法。在 WindowManagerGlobal 内部有如下几个列表比较重要

```java
//存储所有 Window 所对应的 View
private final ArrayList<View> mViews = new ArrayList<View>();

//存储所有 Window 所对应的 ViewRootImpl
private final ArrayList<ViewRootImpl> mRoots = new ArrayList<ViewRootImpl>();

//存储所有 Window 所对应的布局参数
private final ArrayList<WindowManager.LayoutParams> mParams = new ArrayList<WindowManager.LayoutParams>();

//存储了那些正在被删除的 View 对象
private final ArraySet<View> mDyingViews = new ArraySet<View>();
```

addView 方法有如下几步：
1. 检查参数是否合法，如果是**子Window类型**，那么还需要调整一些布局参数
2. 创建 ViewRootImpl 并将 View 添加到列表中。
3. 通过 ViewRootImpl 来更新界面并完成 Window 的添加过程

## Window 的创建过程
### Activity 的 Window 创建过程
Activity 的 Window 创建过程和 Activity 的启动过程有很大联系。顺序上，在 Activity 调用 attach 方法的时候，就开始了 Window 的创建。
下面代码直接实例化了一个 PhoneWindow 对象，并为 Window 设置了很多回调方法，方便在 Window 的状态改变时，回调 Activity
中对应的方法做对应的处理，如我们再熟悉不过的 dispatchTouchEvent、onAttachedToWindow、onDetachedFromWindow、onContentChanged 等方法。

<div align="center">Activity#attach</div>
 
 ```java
final void attach(Context context, ActivityThread aThread,
        Instrumentation instr, IBinder token, int ident,
        Application application, Intent intent, ActivityInfo info,
        CharSequence title, Activity parent, String id,
        NonConfigurationInstances lastNonConfigurationInstances,
        Configuration config, String referrer, IVoiceInteractor voiceInteractor,
        Window window, ActivityConfigCallback activityConfigCallback, IBinder assistToken) {
    attachBaseContext(context);// 将 Activity 关联 ContextImpl
    mFragments.attachHost(null /*parent*/);
    mWindow = new PhoneWindow(this, window, activityConfigCallback);// 直接实例化一个 PhoneWindow
    mWindow.setWindowControllerCallback(this);
    // 由于 Activity 实现了 Window.Callback 接口，因此当 Window 接收到外界的状态改变时就会回调 Activity 中实现的 
    // Callback 接口中的方法，比如我们比较熟悉的 dispatchTouchEvent、onAttachedToWindow、onDetachedFromWindow 等
    mWindow.setCallback(this);
    mWindow.setOnWindowDismissedCallback(this);
    mWindow.getLayoutInflater().setPrivateFactory(this);
    mWindow.setWindowManager(// Window 关联 WMS
            (WindowManager)context.getSystemService(Context.WINDOW_SERVICE),
            mToken, mComponent.flattenToString(),
            (info.flags & ActivityInfo.FLAG_HARDWARE_ACCELERATED) != 0);
}
```

实例化了 PhoneWindow 并不代表 Window 被添加了，Window 添加是一个 IPC 过程，必须通过 WMS 来添加。于是将焦点转移到 PhoneWindow 
的实例化。preservedWindow 中文可以翻译为预先保留的 Window ，也从侧面说明，当前的这个 PhoneWindow 虽然已经被创建了，但是它并没有被
WindowManager 所识别，所以这个时候的 Window 还无法提供具体功能，因为它还无法接收外界的输入信息。下面截取了部分代码，调用了 getDecorView
方法。每一个 Window 都对应 一个View，Window 是 抽象的概念，而 View 是 Window 的具体表现形式，通过源码，是不是感觉对这句话的理解
更加的深刻了呢。

<div align="center">PhoneWindow#PhoneWindow()</div>

```java
public PhoneWindow(Context context, Window preservedWindow,
        ActivityConfigCallback activityConfigCallback) {
    this(context);
    // Only main activity windows use decor context, all the other windows depend on whatever
    // context that was given to them.
    mUseDecorContext = true;
    if (preservedWindow != null) {
        mDecor = (DecorView) preservedWindow.getDecorView();//实例化 DecorView 对象。
    }
    mActivityConfigCallback = activityConfigCallback;
}
```

之后内部调用了 installDecor() 方法

<div align="center">PhoneWindow#getDecorView</div>

```java
@Override
public final @NonNull View getDecorView() {
    if (mDecor == null || mForceDecorInstall) {
        installDecor();
    }
    return mDecor;
}
```

下面是 installDecor 中截取的比较重要的代码，实例化了 DecorView 对象，DecorView 继承了 FrameLayout ，所有它本质上是一个 FrameLayout。
DecorView 内部一定包含一个 id 为 content 的 FrameLayout，用来承载 Activity 的视图。
**特别注意 mContentParent ，它是个 FrameLayout，他的 id 是 android.R.id.content**

<div align="center">PhoneWindow#installDecor</div>

```java
private void installDecor() {
    mForceDecorInstall = false;
    if (mDecor == null) {
        mDecor = generateDecor(-1);// 实例化 DecorView ，并将 PhoneWindow 关联到 DecorView 
        mDecor.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        mDecor.setIsRootNamespace(true);
        if (!mInvalidatePanelMenuPosted && mInvalidatePanelMenuFeatures != 0) {
            mDecor.postOnAnimation(mInvalidatePanelMenuRunnable);
        }
    } else {
        mDecor.setWindow(this);
    }
    if (mContentParent == null) {
        mContentParent = generateLayout(mDecor);// 返回的 mContentParent 是个 FrameLayout，它就是 android.R.id.content
    }
}
```

到目前为止，我们还没有发现 WMS 添加 Window 的相关代码。在 Activity onCreate方法中我们不可避免的调用 setContentView 来设置布局，
而我们设置的这个布局，就是 mContentParent 的唯一子 View。下面代码将我们设置的 View 添加到 mContentParent中。

<div align="center">PhoneWindow#setContentView</div>

```java
@Override
public void setContentView(int layoutResID) {
    if (mContentParent == null) {// mContentParent 未实例化，则先实例化
        installDecor();
    } else if (!hasFeature(FEATURE_CONTENT_TRANSITIONS)) {// 走这里
        mContentParent.removeAllViews();
    }

    if (hasFeature(FEATURE_CONTENT_TRANSITIONS)) {
        final Scene newScene = Scene.getSceneForLayout(mContentParent, layoutResID,
                getContext());
        transitionTo(newScene);
    } else {
        mLayoutInflater.inflate(layoutResID, mContentParent);// 将我们设置的布局添加到mContentParent中
    }
    mContentParent.requestApplyInsets();
    final Callback cb = getCallback();
    if (cb != null && !isDestroyed()) {
        cb.onContentChanged();// 回调，通知 Activity 布局已经加载，可以在 Activity 中重写这个方法做对应的处理
    }
}
```

执行完 onCreate，发现还是没有 WMS 添加 Window 的相关代码，可以猜测，这部分代码肯定在 onCreate 之后，事实证明的确如此。
在 ActivityThread 的 handleResumeActivity 方法中，首先会调用 performResumeActivity 执行 onResume 方法，接着会调用 Activity
的 makeVisible()，正是在 makeVisible 方法中，DecorView 通过 WM 完成了添加和显示这两个过程，到这里 Activity 的视图才能被用户看到。

<div align="center">ActivityThread#handleResumeActivity</div>

```java
public void handleResumeActivity(IBinder token, boolean finalStateRequest, boolean isForward,
            String reason) {
    final ActivityClientRecord r = performResumeActivity(token, finalStateRequest, reason); // 执行 
    if (r.activity.mVisibleFromClient) {
        r.activity.makeVisible();
    }            
}
```

<div align="center">Activity#makeVisible</div>

```java
void makeVisible() {
    if (!mWindowAdded) {
        ViewManager wm = getWindowManager();
        wm.addView(mDecor, getWindow().getAttributes());
        mWindowAdded = true;
    }
    mDecor.setVisibility(View.VISIBLE);
}
```

### Dialog 的 Window 创建过程
### Toast 的 Window 创建过程
Toast 属于系统 Window

## 四大组件的工作过程
Activity 是一种展示型组件，Service 是一种计算型组件，用于在后台执行一系列计算任务，BroadcastReceiver 是一种
消息型组件，用于在不同的组件乃至不同的应用之间传递消息。ContentProvider 是一种数据共享型组件，用于向其他组件
乃至其他应用共享数据。

### Activity 的工作过程
下图是 Activity 的启动流程时序图，调用过程比较复杂，图片建议放大后查看。

![Activity启动流程时序图](http://img.blogss.cn/myBlog/20200907192918145.jpg)

<div align="center">Activity启动流程时序图</div>

Android 9 及其以后 Activity 的启动过程有变化，主要体现在 realStartActivityLocked 方法。将启动一个 Activity 看做是
执行一次事务。当调用 startActivity 那一刻开始，就进行了很深层次的方法调用，不过最终调用还是会回到 ActivityThread
，H 是 ActivityThread 的一个内部类，它继承了 Handler，启动一个 Activity 就是用 H 发送了一个 EXECUTE_TRANSACTION 类型的消息。
H 对象接收到了这个消息就会调用 handleMessage 方法对这个消息进行相应的处理，如下部分源码是对一些消息的处理。

<div align="center">H#handleMessage</div>

```java
public void handleMessage(Message msg) {
    if (DEBUG_MESSAGES) Slog.v(TAG, ">>> handling: " + codeToString(msg.what));
    switch (msg.what) {
        case RECEIVER:
            Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "broadcastReceiveComp");
            handleReceiver((ReceiverData)msg.obj);
            Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
            break;
        case CREATE_SERVICE:
            Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, ("serviceCreate: " + String.valueOf(msg.obj)));
            handleCreateService((CreateServiceData)msg.obj);
            Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
            break;
        case BIND_SERVICE:
            Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "serviceBind");
            handleBindService((BindServiceData)msg.obj);
            Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
            break;
        case UNBIND_SERVICE:
            Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "serviceUnbind");
            handleUnbindService((BindServiceData)msg.obj);
            schedulePurgeIdler();
            Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
            break;
        case EXECUTE_TRANSACTION:
            final ClientTransaction transaction = (ClientTransaction) msg.obj;
            mTransactionExecutor.execute(transaction);
            if (isSystem()) {
                // Client transactions inside system process are recycled on the client side
                // instead of ClientLifecycleManager to avoid being cleared before this
                // message is handled.
                transaction.recycle();
            }
            // TODO(lifecycler): Recycle locally scheduled transactions.
            break;
        case RELAUNCH_ACTIVITY:
            handleRelaunchActivityLocally((IBinder) msg.obj);
            break;
    }
}
```
我们知道启动一个 Activity 就是发送一个 EXECUTE_TRANSACTION 类型的消息，然后由 Handler 来处理。最终的调用是 ClientTransactionItem
各个子类的 execute 方法。比如启动一个 Activity ，那么就是 LaunchActivityItem 中的 execute 方法。

<div align="center">LaunchActivityItem#execute</div>

```java
@Override
public void execute(ClientTransactionHandler client, IBinder token,
        PendingTransactionActions pendingActions) {
    Trace.traceBegin(TRACE_TAG_ACTIVITY_MANAGER, "activityStart");
    ActivityClientRecord r = new ActivityClientRecord(token, mIntent, mIdent, mInfo,
            mOverrideConfig, mCompatInfo, mReferrer, mVoiceInteractor, mState, mPersistentState,
            mPendingResults, mPendingNewIntents, mIsForward,
            mProfilerInfo, client, mAssistToken);
    client.handleLaunchActivity(r, pendingActions, null /* customIntent */);
    Trace.traceEnd(TRACE_TAG_ACTIVITY_MANAGER);
}
```

看到 client.handleLaunchActivity 就知道再次绕回到 ActivityThread#handleLaunchActivity 方法中去了。

<div align="center">ActivityThread#handleLaunchActivity</div>

```java
@Override
public Activity handleLaunchActivity(ActivityClientRecord r,
        PendingTransactionActions pendingActions, Intent customIntent) {
    WindowManagerGlobal.initialize();

    // Hint the GraphicsEnvironment that an activity is launching on the process.
    GraphicsEnvironment.hintActivityLaunch();

    final Activity a = performLaunchActivity(r, customIntent);
    return a;
}
```
handleLaunchActivity 中又调用了 performLaunchActivity。这个方法是启动 Activity 的真正实现，内部实例化了
 Activity 对象、ContextImpl 上下文对象、Application 对象，然后依次调用了 Activity 的 attach 和 onCreate 
 方法，这意味着 Activity 完成了整个启动过程。

<div align="center">ActivityThread#performLaunchActivity</div>

```java
/**  Core implementation of activity launch. */
private Activity performLaunchActivity(ActivityClientRecord r, Intent customIntent) {
    ActivityInfo aInfo = r.activityInfo;
    // 实例化上下文对象
    ContextImpl appContext = createBaseContextForActivity(r);
    Activity activity = null;
    try {
        java.lang.ClassLoader cl = appContext.getClassLoader();
        // 实例化 activity
        activity = mInstrumentation.newActivity(
                cl, component.getClassName(), r.intent);
        StrictMode.incrementExpectedActivityCount(activity.getClass());
        r.intent.setExtrasClassLoader(cl);
        r.intent.prepareToEnterProcess();
        if (r.state != null) {
            r.state.setClassLoader(cl);
        }
    }

    try {
        // 实例化 Application 对象，该对象在整个应用程序中只有一个，如果已经创建过，则不会再次创建
        Application app = r.packageInfo.makeApplication(false, mInstrumentation);
        if (activity != null) {
            // 调用 activity 的 attach 的方法，建立 activity 与 ContextImpl 的关联。除此之外，在 attach 还会完成 Window 的创建
            //并建立自己和 Window 的关联，这样当 Window 接收到外部输入事件后就可以将事件传递给 Activity。
            activity.attach(appContext, this, getInstrumentation(), r.token,
                    r.ident, app, r.intent, r.activityInfo, title, r.parent,
                    r.embeddedID, r.lastNonConfigurationInstances, config,
                    r.referrer, r.voiceInteractor, window, r.configCallback,
                    r.assistToken);
            activity.mCalled = false;
            if (r.isPersistable()) {
                mInstrumentation.callActivityOnCreate(activity, r.state, r.persistentState);
            } else {//调用 Activity 的 onCreate 方法
                mInstrumentation.callActivityOnCreate(activity, r.state);
            }
            r.activity = activity;
        }
        r.setState(ON_CREATE);
    }
    return activity;
}
```