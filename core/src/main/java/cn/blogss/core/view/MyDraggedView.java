package cn.blogss.core.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @创建人 560266
 * @文件描述 自定义可全屏拖动的View，使用属性动画完成
 * @创建时间 2020/5/14
 */
public class MyDraggedView extends View {
    private static final String TAG = "MyDraggedView";

    private float mLastX, mLastY;

    public MyDraggedView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /*getRawX/getRawY返回的是相对于手机屏幕左上角的x和y坐标*/
        float x = event.getRawX();
        float y = event.getRawY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:{
                float deltaX = x - mLastX;
                float deltaY = y - mLastY;
                Log.d(TAG,"move, deltaX:"+deltaX+" deltaY:"+deltaY);
                float translationX = getTranslationX() + deltaX;
                float translationY = getTranslationY() + deltaY;
                setTranslationX(translationX);
                setTranslationY(translationY);
                break;
            }
            case MotionEvent.ACTION_UP:
                break;
        }
        mLastX = x;
        mLastY = y;
        return true;
    }


}
