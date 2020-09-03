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
### Dialog 的 Window 创建过程
### Toast 的 Window 创建过程
Toast 属于系统 Window

## 四大组件的工作过程
Activity 是一种展示型组件，Service 是一种计算型组件，用于在后台执行一系列计算任务，BroadcastReceiver 是一种
消息型组件，用于在不同的组件乃至不同的应用之间传递消息。ContentProvider 是一种数据共享型组件，用于向其他组件
乃至其他应用共享数据。

### Activity 的工作过程
startActivity(intent) 即可启动一个 Activity。startActivity 方法有好几种重载的方式，但它们终究会调用 startActivityForResult 方法

<div align="center">Activity#startActivityForResult</div>

```java
public void startActivityForResult(@RequiresPermission Intent intent, int requestCode,
            @Nullable Bundle options) {
    if (mParent == null) {
        options = transferSpringboardActivityOptions(options);
        Instrumentation.ActivityResult ar =
            mInstrumentation.execStartActivity(
                this, mMainThread.getApplicationThread(), mToken, this,
                intent, requestCode, options);
        if (ar != null) {
            mMainThread.sendActivityResult(
                mToken, mEmbeddedID, requestCode, ar.getResultCode(),
                ar.getResultData());
        }
        if (requestCode >= 0) {
            // If this start is requesting a result, we can avoid making
            // the activity visible until the result is received.  Setting
            // this code during onCreate(Bundle savedInstanceState) or onResume() will keep the
            // activity hidden during this time, to avoid flickering.
            // This can only be done when a result is requested because
            // that guarantees we will get information back when the
            // activity is finished, no matter what happens to it.
            mStartedActivity = true;
        }

        cancelInputsAndStartExitTransition(options);
        // TODO Consider clearing/flushing other event sources and events for child windows.
    } else {
        if (options != null) {
            mParent.startActivityFromChild(this, intent, requestCode, options);
        } else {
            // Note we want to go through this method for compatibility with
            // existing applications that may have overridden it.
            mParent.startActivityFromChild(this, intent, requestCode);
        }
    }
}
```