package cn.blogss.core.view.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;

import cn.blogss.core.R;

/**
 * 多个 TextView 组合控件
 */

public class TextViewGroup extends ViewGroup {
    private static final String TAG = "TextViewGroup";

    private Paint mPaint;
    private Path mPath;
    private Path mTempPath;
    private RectF rectF;

    private Xfermode xfermode;

    // Four corners x and y radius.
    private final float[] radii = {
            0,0,    // top-left
            0,0,    // top-right
            0,0,    // bottom-right
            0,0     // bottom-left
    };

    private int textViewCount = 0;
    private int radius = 0;
    private int topLeftRadius = 0;
    private int topRightRadius = 0;
    private int bottomRightRadius = 0;
    private int bottomLeftRadius = 0;
    private int cornerPosition = -1;
    public static final int TOP_LEFT = 1;
    public static final int TOP_RIGHT = 2;
    public static final int BOTTOM_RIGHT = 4;
    public static final int BOTTOM_LEFT = 8;

    public TextViewGroup(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public TextViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    /**
     * 获取自定义属性
     * @param attrs
     */
    private void init(AttributeSet attrs) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPath = new Path();
        mTempPath = new Path();
        rectF = new RectF();
        xfermode = new PorterDuffXfermode(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PorterDuff.Mode.DST_OUT : PorterDuff.Mode.DST_IN);

        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.TextViewGroup,0,0);
        int attrCount = typedArray.getIndexCount();
        Log.i(TAG, "attrCount: " + attrCount);

        for(int i=0;i<attrCount;i++){
            int index = typedArray.getIndex(i);
            Log.i(TAG, "index: " + index);
            if(index == R.styleable.TextViewGroup_textView_count){
                textViewCount = typedArray.getInt(i,textViewCount);
            } else if(index == R.styleable.TextViewGroup_corner_radius){
                radius = typedArray.getDimensionPixelSize(i,radius);
            } else if(index == R.styleable.TextViewGroup_topLeft_radius){
                topLeftRadius = typedArray.getDimensionPixelSize(i,topLeftRadius);
            } else if(index == R.styleable.TextViewGroup_topRight_radius){
                topRightRadius = typedArray.getDimensionPixelSize(i,topRightRadius);
            } else if(index == R.styleable.TextViewGroup_bottomRight_radius){
                bottomRightRadius = typedArray.getDimensionPixelSize(i,bottomRightRadius);
            } else if(index == R.styleable.TextViewGroup_bottomLeft_radius){
                bottomLeftRadius = typedArray.getDimensionPixelSize(i,bottomLeftRadius);
            } else if(index == R.styleable.TextViewGroup_corner_position){
                cornerPosition = typedArray.getInt(i,cornerPosition);
            }
        }

        for (int i=0;i<textViewCount;i++){
            TextView view = new TextView(getContext());
            addView(view);
        }

        setRadii();
        typedArray.recycle();

        Log.i(TAG, "textViewCount: " + textViewCount);
        Log.i(TAG, "radius: " + radius);
        Log.i(TAG, "cornerPosition: " + cornerPosition);
    }

    /**
     * 测试自身的宽高
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int visibleChildCount = 0;

        Log.i(TAG, "width: " + width);
        if(textViewCount == 0){
            width = height = 0;
        }else{
            for (int i=0;i<getChildCount();i++){
                if(getChildAt(i).getVisibility() == VISIBLE){
                    visibleChildCount++;
                }
                getChildAt(i).getLayoutParams().width = width/getChildCount();
                getChildAt(i).getLayoutParams().height = height;
            }
            measureChildren(widthMeasureSpec,heightMeasureSpec);
        }

        setMeasuredDimension(width*visibleChildCount/getChildCount(),height);
    }

    /**
     * 合理摆放子 View 的位置
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int childLeft = 0;
        int childWidth = 0;

        for (int i=0;i<childCount;i++){
            View childView = getChildAt(i);
            childWidth = childView.getMeasuredWidth();
            Log.i(TAG, i+" childWidth: "+childWidth);
            if(childView.getVisibility() == VISIBLE){
                childView.layout(childLeft,0,childLeft+childWidth,childView.getMeasuredHeight());
                childLeft += childWidth;
            }
        }
    }

    /**
     * 绘制圆角
     * @param canvas
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.saveLayer(rectF,null);
        super.dispatchDraw(canvas);
        mPaint.setXfermode(xfermode);   // 图像混合，去除黑色
        mTempPath.addRect(rectF,Path.Direction.CCW);
        mPath.addRoundRect(rectF,radii,Path.Direction.CCW);
        mTempPath.op(mPath,Path.Op.DIFFERENCE); //区域处理，保留mTempPath-mPath的区域
        canvas.drawPath(mTempPath,mPaint);
        mPaint.setXfermode(null);
        canvas.restore();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.i(TAG, "onSizeChanged, w: " + w + ", h: " + h);
        rectF.set(0,0,w,h);
    }

    private void setRadii() {
        if(radius != 0){
            Arrays.fill(radii, radius);
            return;
        }

        if(containPosition(TOP_LEFT)){  // top-left
            radii[0] = topLeftRadius;
            radii[1] = topLeftRadius;
        }
        if(containPosition(TOP_RIGHT)){  // top-right
            radii[2] = topRightRadius;
            radii[3] = topRightRadius;
        }
        if(containPosition(BOTTOM_RIGHT)){  // bottom-right
            radii[4] = bottomRightRadius;
            radii[5] = bottomRightRadius;
        }
        if(containPosition(BOTTOM_LEFT)){  // bottom-left
            radii[6] = bottomLeftRadius;
            radii[7] = bottomLeftRadius;
        }
    }

    private boolean containPosition(int position) {
        return (position & cornerPosition) == position;
    }

    /**
     * 设置子 View 背景颜色
     * @param textViewColor Color resource ID array.
     */
    public void setChildrenBackgroundColor(int[] textViewColor) {
        if(textViewColor.length > textViewCount){
            throw new IllegalArgumentException("Max textViewColor.length is " + textViewCount + ", now is " + textViewColor.length);
        }
        for (int i=0;i<textViewColor.length;i++){
            View view = getChildAt(i);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(getContext().getResources().getColor(textViewColor[i]));
            view.setBackground(drawable);
        }
    }

    /**
     * 设置子 View 文本显示
     * @param childrenText child text.
     */
    public void setChildrenText(int[] childrenText){
        if(childrenText.length > textViewCount){
            throw new IllegalArgumentException("Max childrenText.length is " + textViewCount + ", now is " + childrenText.length);
        }
        for (int i=0;i<childrenText.length;i++){
            TextView view = (TextView) getChildAt(i);
            view.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            view.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
            view.setText(childrenText[i]);
        }
    }

    /**
     * 设置文字大小
     * @param sp size unit
     */
    public void setChildrenTextSize(int sp){
        for (int i=0;i<textViewCount;i++){
            TextView view = (TextView) getChildAt(i);
            view.setTextSize(sp);
        }
    }

    /**
     * 设置子 View 文本颜色
     * @param childrenTextColor Color resource ID array.
     */
    public void setChildrenTextColor(int[] childrenTextColor){
        if(childrenTextColor.length > textViewCount){
            throw new IllegalArgumentException("TextViewCount is less than childrenTextColor.length!");
        }
        for (int i=0;i<childrenTextColor.length;i++){
            TextView view = (TextView) getChildAt(i);
            view.setTextColor(getContext().getResources().getColor(childrenTextColor[i]));
        }
    }

    /**
     * 批量隐藏子 view
     * @param index 子 view 索引(0,1,2...)
     */
    public void setInVisibleView(int[] index){
        if(index.length > textViewCount){
            throw new IllegalArgumentException("Max index.length is " + textViewCount + ", now is " + index.length);
        }

        for (int i=0;i<textViewCount;i++){
            getChildAt(i).setVisibility(VISIBLE);
        }

        for (int i=0;i<index.length&&index[i]<textViewCount;i++){
            getChildAt(index[i]).setVisibility(GONE);
        }
    }


    public void setVisible(int index){
        if(index >= textViewCount){
            throw new IllegalArgumentException("Max index is " + (textViewCount-1) + ", now is " + index);
        }
        getChildAt(index).setVisibility(VISIBLE);
    }

    public void setInvisible(int index){
        if(index >= textViewCount){
            throw new IllegalArgumentException("Max index is " + (textViewCount-1) + ", now is " + index);
        }
        getChildAt(index).setVisibility(GONE);
    }
}
