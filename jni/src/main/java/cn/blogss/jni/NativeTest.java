package cn.blogss.jni;

import android.util.Log;

public class NativeTest {

    private static final String TAG = "NativeTest";

    static {
        Log.e(TAG, System.getProperty("java.library.path"));
        System.loadLibrary("math-lib");
    }

    public native void print99();

    public native void bubbleSort(int[] arr);

}
