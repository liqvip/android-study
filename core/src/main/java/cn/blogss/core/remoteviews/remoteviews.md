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