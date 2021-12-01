package cn.blogss.helper;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * @创建人 560266
 * @文件描述    状态栏兼容工具类
 * @创建时间 2020/4/9
 */
public class StatusBarCompatUtil {
    /**
     * @param activity
     * @param color
     */
    public static void compat(Activity activity,int color){
        int sdkVersion = Build.VERSION.SDK_INT;
        Window window = activity.getWindow();
        if(sdkVersion >= Build.VERSION_CODES.LOLLIPOP){/*兼容Android 5.0以上*/
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //注意要清除 FLAG_TRANSLUCENT_STATUS flag
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
        }

        if(sdkVersion >= Build.VERSION_CODES.M){/*兼容Android 6.0以上*/
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    /**
     * 隐藏状态栏
     * @param activity
     */
    public static void hide(Activity activity){
        Window window = activity.getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}
