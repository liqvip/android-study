IPC 是 Inter-Process Communication 的缩写，含义为进程间通信或者跨进程通信，是指两个进程之间进行数据交换的过程。

在 Android 中使用多进程只有一种方法，那就是给四大组件(Activity、Service、Receiver、ContentProvider)在
AndroidManifest中指定 `android:process` 属性。

所有运行在不同进程中的四大组件，只要他们之间需要通过内存来共享数据，都会共享失败，这也是多进程带来的主要影响。
一般来说，使用多进程会造成如下几个方面的问题：
1. 静态成员和单例模式完全失效。
2. 线程同步机制完全失效。
3. SharedPreferences 可靠性下降。（SharedPreferences 不支持两个进程同时去执行写操作，否则会导致一定几率的数据丢失）
4. Application 会多次创建。
 