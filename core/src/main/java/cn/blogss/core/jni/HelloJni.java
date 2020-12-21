package cn.blogss.core.jni;

/**
 * @author LiQiang
 * @description JNI 示例程序
 * @date 2020/12/10
 */
public class HelloJni {

    static {
        System.out.println(System.getProperty("java.library.path"));
        System.loadLibrary("libHelloJni");
    }

    public static void main(String[] args) {
        HelloJni helloJni = new HelloJni();
        System.out.println(helloJni.get());
        helloJni.set("hello jni !");
    }

    public native String get();

    public native void set(String str);
}
