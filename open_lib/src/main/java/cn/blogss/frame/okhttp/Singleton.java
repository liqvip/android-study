package cn.blogss.frame.okhttp;

/**
 * 懒汉式单例
 * 非线程安全
 */
public class Singleton {
    private static Singleton singleton;
    private Singleton(){}

    public static Singleton getInstance() {
        if(singleton == null) {
            singleton = new Singleton();
        }
        return singleton;
    }
}

/**
 * 懒汉式单例
 * 线程安全
 */
class Singleton1 {
    private static Singleton1 singleton;
    private Singleton1(){}

    public static synchronized Singleton1 getInstance() {
        if(singleton == null) {
            singleton = new Singleton1();
        }
        return singleton;
    }
}

/**
 * 双重检查锁
 * 线程安全
 */
class Singleton2 {
    private volatile static Singleton2 singleton;
    private Singleton2(){}

    public static Singleton2 getInstance() {
        if(singleton == null) {  // 第一次检查
            synchronized (Singleton2.class) {
                if(singleton == null) { // 第二次检查
                    singleton = new Singleton2();
                }
            }
        }
        return singleton;
    }
}

/**
 * 内部静态类(推荐使用)
 */
class Singleton3 {
    private Singleton3(){}

    public static Singleton3 getInstance() {
        return Holder.INSTANCE;
    }

    static class Holder {
        private static final Singleton3 INSTANCE = new Singleton3();
    }
}

