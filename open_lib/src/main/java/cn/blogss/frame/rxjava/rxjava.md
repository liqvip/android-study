RxJava 项目地址：[https://github.com/ReactiveX/RxJava](https://github.com/ReactiveX/RxJava)

RxAndroid 项目地址：[https://github.com/ReactiveX/RxAndroid](https://github.com/ReactiveX/RxAndroid)
 
 ## 线程控制——Scheduler（调度器）
 在不指定线程的情况下， RxJava 遵循的是线程不变的原则，即：在哪个线程调用 subscribe()，就在哪个线程生产事件；
 在哪个线程生产事件，就在哪个线程消费事件。如果需要切换线程，就需要用到 Scheduler （调度器）。
 相当于线程控制器，RxJava 通过它来指定每一段代码应该运行在什么样的线程。RxJava 已经内置了几个 Scheduler ，
 它们已经适合大多数的使用场景：
 1. ComputationScheduler：计算所使用的 Scheduler。这个计算指的是 CPU 密集型计算，
 即不会被 I/O 等操作限制性能的操作，例如图形的计算。这个 Scheduler 使用的固定的线程池，大小为 CPU 核数。
 不要把 I/O 操作放在 computation() 中，否则 I/O 操作的等待时间会浪费 CPU。
 2. ExecutorScheduler：
 3. HandlerScheduler
 4. ImmediateThinScheduler：直接在当前线程运行，相当于不指定线程。这是默认的 Scheduler
 5. IoScheduler：I/O 操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler。
 行为模式和 newThread() 差不多，区别在于 io() 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，
 因此多数情况下 io() 比 newThread() 更有效率。不要把计算工作放在 io() 中，可以避免创建不必要的线程。
 6. NewThreadScheduler：总是启用新线程，并在新线程执行操作。
 7. SchedulerWhen
 8. SingleScheduler
 9. TestScheduler
 10. TrampolineScheduler
 
 另外，Android 还有 2 个专用的调度器：
 1. AndroidSchedulers：它指定的操作将在 Android 主线程运行。
 2. HandlerScheduler