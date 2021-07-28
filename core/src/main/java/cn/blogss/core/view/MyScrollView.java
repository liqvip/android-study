package cn.blogss.core.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;

import androidx.annotation.Nullable;

/**
 * @创建人 560266
 * @文件描述 弹性滑动，使用Scroller实现
 * @创建时间 2020/5/14
 */
public class MyScrollView extends LinearLayout {
    private Scroller mScroller;

    public MyScrollView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }


    /*缓慢移动到指定位置*/
    public void smoothScrollTo(int destX,int destY){
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        int deltaX = destX - scrollX;
        int deltaY = destY - scrollY;
        /*1000ms 内滑向destX，效果就是缓慢滑动*/
        mScroller.startScroll(scrollX,0,deltaX,deltaY,3000);
        // invalidate方法会导致View重绘，在View的draw方法中又会去调用computeScroll方法。接着调用postInvalidate方法来进行第二次重绘，如此反复，直到整个滑动过程结束。
        invalidate();   // 重绘
    }

    @Override
    public void computeScroll() {
        if(mScroller.computeScrollOffset()){    // 滑动未结束
            /*scrollTo 基于所传递参数的绝对滑动*/
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();   // 二次重绘
        }
    }
}
