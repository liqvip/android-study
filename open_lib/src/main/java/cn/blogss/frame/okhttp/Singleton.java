package cn.blogss.frame.okhttp;

/**
 * 懒汉式单例
 * 非线程安全
 */
public class Singleton {
    private static Singleton singleton;
    private Singleton(){}

    public Singleton getInstance() {
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
    private static volatile Singleton1 singleton;
    private Singleton1(){}

    public Singleton1 getInstance() {
        if(singleton == null) {
            singleton = new Singleton1();
        }
        return singleton;
    }
}
