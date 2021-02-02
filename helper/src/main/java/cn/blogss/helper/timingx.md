> TimingX 是一个用于 Android 多组件同步计时的一个工具类，使用 Handler 实现。
在项目中我们经常有计时的需求，具体表现为从00:00开始计时，即分秒的格式。TimingX 是
用于计时的，它目前不支持倒计时，不过如果你有这个需求也可以很容易的实现。

## 项目地址
Gitee：[Timingx](https://gitee.com/lishu1108/android_study/blob/master/helper/src/main/java/cn/blogss/helper/TimingX.java)
同样欢迎大家访问我的个人博客：[Thatcher Li](www.blogss.cn)，高质量原创技术文章分享。

## 基本用法
1. TimingX 的使用非常简单，整个类的源码不到200行，当一个控件要显示计时，只需要下面一句代码即可添加到计时控件组
```java
TimingX.builder().add(view);
```

2. 开始计时
```java
TimingX.builder().start();
```
如果你不想将添加到计时控件组和开始计时分开成两句代码，TimingX 也同样支持链式调用
```java
TimingX.builder().add(view).start();
```
注意一旦调用 `start()` 方法后，所有添加到控件组的控件都开始了计时，它们的时间是一样的。

3. 停止计时
```java
TimingX.builder().stop();
```
4. 销毁 Handler，避免内存泄漏
当你不需要计时了，比如退出 `Activity` 或 `Fragment`，记得在他们的`onDestroy()`生命周期方法中调用
```java
TimingX.builder().destroy();
```

## getStatus()方法
对外公开的方法，该方法可以获取当前计时的状态，开始/暂停。很多时候你会用到它，去处理自己的业务逻辑。

## 总结
如果你有更好的想法，目前的功能满足不了你的业务需求，可以给我留言。又或者你在使用过程中遇到了
问题，可以提`issue`，与此同时，你可以贡献源代码，为此类扩展功能。