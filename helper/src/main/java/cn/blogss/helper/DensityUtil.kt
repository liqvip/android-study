package cn.blogss.helper

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager

/**
 * @创建人 560266
 * @文件描述 单位转换工具类
 * @创建时间 2020/9/18
 */

/**
 * dp 转化为 px
 */
fun dp2px(context: Context, dpVal: Float): Int{
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.resources.displayMetrics).toInt()
}

/**
 * px 转化为 dp
 */
fun px2dp(context: Context, pxVal: Float): Int{
    val density = context.resources.displayMetrics.density
    return (pxVal/density).toInt()
}


/**
 * 获取屏幕信息(尺寸/大小/密度等)
 * @param context Context
 * @return DisplayMetrics
 */
fun getScreenMetrics(context: Context): DisplayMetrics{
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val dm = DisplayMetrics()
    wm.defaultDisplay.getMetrics(dm)
    return dm
}

