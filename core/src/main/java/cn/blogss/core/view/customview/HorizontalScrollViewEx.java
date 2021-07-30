package cn.blogss.core.view.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 类似水平布局的 LinearLayout, 但其可以水平滑动
 */
public class HorizontalScrollViewEx extends ViewGroup {
    private static final String TAG = "HorizontalScrollViewEx";
    private int mChildrenSize;
    private int mChildWidth;
    private int mChildIndex;

    // 上次滑动的坐标
    private int mLastX = 0;
    private int mLastY = 0;

    // 上次滑动的坐标
    private int mLastXIntercept = 0;
    private int mLastYIntercept = 0;

    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;

    public HorizontalScrollViewEx(Context context) {
        super(context);
        init();
    }


    public HorizontalScrollViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HorizontalScrollViewEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public HorizontalScrollViewEx(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        if(mScroller == null){
            mScroller = new Scroller(getContext());
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    /**
     * 测试子view和自身
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = 0;
        int measuredHeight = 0;

        //  1. 获取子view数量，对所有子view开始测量
        final int childCount = getChildCount();
        measureChildren(widthMeasureSpec,heightMeasureSpec);

        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

        // 2. 处理宽高采用了 warp_content 的情况
        if(childCount == 0){    // 没有子元素
            if(widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST){  // (wrap_content,wrap_content)
                setMeasuredDimension(0, 0);
            }else if(widthMeasureSpec == MeasureSpec.AT_MOST){
                setMeasuredDimension(0, heightSpecSize);
            }else if(heightMeasureSpec == MeasureSpec.AT_MOST){
                setMeasuredDimension(widthSpecSize, 0);
            }
        }else if(widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST){    // (wrap_content,wrap_content)宽为所有子view的宽，高为第一个子view 的高度
            final View childView = getChildAt(0);
            measuredWidth = childView.getMeasuredWidth() * childCount;
            measuredHeight = childView.getMeasuredHeight();
            setMeasuredDimension(measuredWidth,measuredHeight);
        }else if(widthMeasureSpec == MeasureSpec.AT_MOST){  // (wrap_content,...)
            final View childView = getChildAt(0);
            measuredWidth = childView.getMeasuredWidth() * childCount;
            setMeasuredDimension(measuredWidth,heightSpecSize);
        }else if(heightMeasureSpec == MeasureSpec.AT_MOST){ // (...,wrap_content)
            final View childView = getChildAt(0);
            measuredHeight = childView.getMeasuredHeight();
            setMeasuredDimension(widthSpecSize,measuredHeight);
        }
    }

    /**
     * 对子view布局
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft = 0;
        final int childCount = getChildCount();
        mChildrenSize = childCount;

        for(int i=0; i<childCount; i++){
            final View childView = getChildAt(i);
            if(childView.getVisibility() != View.GONE){
                final int childWidth = childView.getMeasuredWidth();
                mChildWidth = childWidth;
                childView.layout(childLeft,0,childLeft+childWidth,childView.getMeasuredHeight());
                childLeft += childWidth;
            }
        }
    }

    /**
     * 外部拦截法，解决外部水平滑动，内部垂直滑动产生的滑动冲突
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = false;
        int action = ev.getAction();
        // 点击点的坐标
        int x = (int)ev.getX();
        int y = (int)ev.getY();


        if(action == MotionEvent.ACTION_DOWN){
            intercepted = false;
            // 避免水平滑动未结束时，用户突然进行另外一个触摸操作。即将下一个序列的点击事件仍然交给本 view 处理
            if(!mScroller.isFinished()){
                mScroller.abortAnimation();
                intercepted = true;
            }
        // 滑动规则：根据用户水平和垂直方向滑动的距离大小，判断用户是水平滑动还是垂直滑动。水平滑动则本view处理，垂直滑动则交给子view
        }else if(action == MotionEvent.ACTION_MOVE){
            int deltaX = x - mLastXIntercept;
            int deltaY = y - mLastYIntercept;
            if(Math.abs(deltaX) > Math.abs(deltaY)){
                intercepted = true;
            }else{
                intercepted = false;
            }
        }else if(action == MotionEvent.ACTION_UP){
            intercepted = false;
        }

        mLastX = x;
        mLastY = y;
        mLastXIntercept = x;
        mLastYIntercept = y;
        Log.i(TAG, "intercepted: " + intercepted + ", action: " + ev.getAction() + ", mLastXIntercept: " + mLastYIntercept + ", mLastYIntercept: " + mLastYIntercept);

        return intercepted;
    }

    /**
     * 处理滑动事件
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        mVelocityTracker.addMovement(event);
        int x = (int)event.getX();
        int y = (int)event.getY();
        Log.i(TAG, "action: " + event.getAction());
        Log.i(TAG, "eventXY： "+"("+x+","+y+")");
        Log.i(TAG, "scrollXY："+"("+getScrollX()+","+getScrollY()+")");

        int action = event.getAction();
        if(action == MotionEvent.ACTION_DOWN){
            if(!mScroller.isFinished()){    // 滑动未结束
                mScroller.abortAnimation();
            }
        }else if(action == MotionEvent.ACTION_MOVE){
            int deltaX = x- mLastX;
            int deltaY = y- mLastY;
            scrollBy(-deltaX,0);    // 相对滑动(相对于当前位置)
        }else if(action == MotionEvent.ACTION_UP){
            int scrollX = getScrollX();
            mVelocityTracker.computeCurrentVelocity(1000);  // 计算滑动速度 px/1s
            float xVelocity = mVelocityTracker.getXVelocity();
            float yVelocity = mVelocityTracker.getYVelocity();
            Log.i(TAG, "xyVelocity: "+"("+xVelocity+","+yVelocity+")");
            if(Math.abs(xVelocity) >= 50){  // 滑动速度大于 50px/s，左滑就到达下一个view，右滑就到达上一个view
                // xVelocity > 0，右滑
                mChildIndex = xVelocity > 0 ? mChildIndex - 1 : mChildIndex + 1;
            }else{  // 滑动速度小于 50px/s
                mChildIndex = (scrollX + mChildWidth / 2) / mChildWidth;
            }
            mChildIndex = Math.max(0, Math.min(mChildIndex,mChildrenSize - 1)); //
            int dx = mChildIndex * mChildWidth + (-scrollX);    // 手指松开屏幕后，定位某个子view靠屏幕左侧
            smoothScrollTo(dx,0);
            mVelocityTracker.clear();
        }
        mLastX = x;
        mLastY = y;
        return true;
    }

    /**
     * 开始滑动到指定位置
     * @param dx 水平滑动的距离
     * @param dy 垂直滑动的距离
     */
    private void smoothScrollTo(int dx, int dy){
        mScroller.startScroll(getScrollX(),0,dx,0,500);
        invalidate();   // 重绘，调用 computeScroll 实现滑动
    }

    /**
     * 实现滑动
     */
    @Override
    public void computeScroll() {
        if(mScroller.computeScrollOffset()){    // 滑动未结束
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();   // 二次重绘
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        mVelocityTracker.recycle();
        super.onDetachedFromWindow();
    }
}
