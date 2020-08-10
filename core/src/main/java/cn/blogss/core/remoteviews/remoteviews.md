RemoteViews 和远程 Service 是一样的，RemoteViews 表示的是一个 View 结构，它可以在其他进程中显示，由于它在其他进程中显示，
为了更新它的界面，RemoteViews 提供了一组基础的操作作用于跨进程更新它的界面。
### RemoteViews 的应用

#### RemoteViews 在通知栏上的应用
#### RemoteViews 在桌面小部件上的应用
AppWidgetProvider 是 Android 中提供用于实现桌面小部件的类。其本质是一个广播，即 BroadcastReceiver。桌面小部件的
开发步骤分为如下几步。
1. 定义小部件界面
2. 定义小部件配置信息
3. 定义小部件的实现类
4. 在 AndroidManifest.xml 中声明小部件

#### PendingIntent 概述
PendingIntent 是在将来的某个待定的时刻发生。PendingIntent 的典型应用场景是给 RemoteViews 添加单击事件，因为 RemoteViews 运行在
远程进程中，因此 RemoteViews 不同于普通的 View ，所以无法直接像 View 那样通过 setOnclickListener 方法来设置单击事件。要想给 RemoteViews 
设置单击事件，就必须使用 PendingIntent，PendingIntent 通过 send 和 cancel 方法来发送和取消特定的待定 Intent。

### RemoteViews 的内部机制
RemoteViews 的作用是在其他进程中显示并更新 View 界面。通知栏和桌面小部件中的布局文件实际上是在 NotificationManagerService 以及 
AppWidgetService 中被加载的，而它们运行在系统的 SystemServer 中，这就和我们的进程构成了跨进程通信的场景。
