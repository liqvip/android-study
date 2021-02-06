IPC 是 Inter-Process Communication 的缩写，含义为进程间通信或者跨进程通信，是指两个进程之间进行数据交换的过程。

在 Android 中使用多进程只有一种方法，那就是给四大组件(Activity、Service、Receiver、ContentProvider)在
AndroidManifest中指定 `android:process` 属性。

所有运行在不同进程中的四大组件，只要他们之间需要通过内存来共享数据，都会共享失败，这也是多进程带来的主要影响。
一般来说，使用多进程会造成如下几个方面的问题：
1. 静态成员和单例模式完全失效。
2. 线程同步机制完全失效。
3. SharedPreferences 可靠性下降。（SharedPreferences 不支持两个进程同时去执行写操作，否则会导致一定几率的数据丢失）
4. Application 会多次创建。

虽然说不能直接地共享内存，但是通过跨进程通信我们还是可以实现数据交互。实现跨进程通信的方式很多，比如通过 Intent 
来传递数据，共享文件和 SharedPreferences，基于 Binder 的 Messenger 和 AIDL 以及 Socket 等。
 
## IPC 基础概念
Serializable 接口和 Parcelable 接口可以完成对象的序列化和反序列化过程。当我们需要通过 Intent 和 Binder 传输数据
时就要使用 Parcelable 或者 Serializable 

### Serializable 接口
Serializable 接口是 Java 提供的一个序列化接口，它是一个空接口，为对象提供标准的序列化和反序列化。
serialVersionUID 不是必须的，不声明 serialVersionUID 同样可以实现序列化，但是这将会对反序列化
过程产生影响。

### Parcelable 接口
系统已经为我们提供了许多实现了 Parcelable 接口的类，它们都是可以直接序列化的，比如 Intent、Bundle、Bitmap 等。

### Serializable 与 Parcelable 接口的区别
1. Serializable 是 Java 中的序列化接口，使用起来简单但是开销大，序列化和反序列化过程需要大量I/O操作。
2. Parcelable 是 Android 中的序列化方式，因此更适合用在 Android 平台上，效率很高，主要用在内存序列化上。

### Binder
直观来说，Binder 是 Android 中的一个类，它实现了 IBinder 接口；从 IPC 角度来说，
Binder 是 Android 中的一种跨进程通信方式；Binder 还可以理解为一种虚拟的物理设备，它的设备驱动是 /dev/binder ，
该通信方式在 linux 中没有；从 Android Framework 角度来说，Binder 是 ServiceManager 连接各种 Manager(
ActivityManager、WindowManager，等等)和相应 ManagerService 的桥梁；从 Android 应用层来说，Binder 是客户端
和服务端进行通信的媒介。


## Android 中的 IPC 方式

### 使用 AIDL

### 使用 Socket
