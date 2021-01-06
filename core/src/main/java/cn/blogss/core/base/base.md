该包下的类是一些基类，封装了一些 Android API ，添加了一些特有的行为。
当你新建一个 Activity 或者其他类型的组件时，需要用到这些基类的特性，
可以继承他们。让代码复用<br>

本项目使用 androidx 替代 V7，区别请参考：<br>
[Android Support v4\v7\v13和AndroidX的区别及应用场景](https://blog.csdn.net/csdn_aiyang/article/details/80859771)

[Androidx初尝及其新旧包对照表](https://www.jianshu.com/p/1466ebefe4d0)

[Android关于沉浸式状态栏总结](https://juejin.im/post/5989ded56fb9a03c3b6c8bde#heading-2)

### CrashHandler
该类用于获取应用崩溃信息的基础组件，原理是为线程设置 `UncaughtExceptionHandler`回调接口。当因为一个未捕捉到的异常而导致线程结束运行
，Java 虚拟机就会回调 `UncaughtExceptionHandler` 接口中的 `uncaughtException(Thread t, Throwable e)`方法，
因此只要重写该方法，将应用的崩溃信息写到本地并上传到服务器，开发人员就可以分析崩溃日志，及时发现和解决问题。