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
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setXfermode(xfermode);

        mPath = new Path();
        mTempPath = new Path();
        rectF = new RectF();

        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.TextViewGroup,0,0);
        int attrCount = typedArray.getIndexCount();
        Log.i(TAG, "attrCount: " + attrCount);

        for(int i=0;i<attrCount;i++){
            int index = typedArray.getIndex(i);
            Log.i(TAG, "index: " + index);
            if(index == R.styleable.TextViewGroup_textView_count){
                textViewCount = typedArray.getInt(index,textViewCount);
            } else if(index == R.styleable.TextViewGroup_corner_radius){
                radius = typedArray.getDimensionPixelSize(index,radius);
            } else if(index == R.styleable.TextViewGroup_topLeft_radius){
                topLeftRadius = typedArray.getDimensionPixelSize(index,topLeftRadius);
            } else if(index == R.styleable.TextViewGroup_topRight_radius){
                topRightRadius = typedArray.getDimensionPixelSize(index,topRightRadius);
            } else if(index == R.styleable.TextViewGroup_bottomRight_radius){
                bottomRightRadius = typedArray.getDimensionPixelSize(index,bottomRightRadius);
            } else if(index == R.styleable.TextViewGroup_bottomLeft_radius){
                bottomLeftRadius = typedArray.getDimensionPixelSize(index,bottomLeftRadius);
            } else if(index == R.styleable.TextViewGroup_corner_position){
                cornerPosition = typedArray.getInt(index,cornerPosition);
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

        Log.i(TAG, "width: " + width);

        for (int i=0;i<getChildCount();i++){
            getChildAt(i).getLayoutParams().width = width/getChildCount();
            getChildAt(i).getLayoutParams().height = height;
        }

        measureChildren(widthMeasureSpec,heightMeasureSpec);
        setMeasuredDimension(width,height);
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
     * 离屏缓冲
     * @param canvas
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        int saved = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);
        drawCorner(canvas);
        canvas.restoreToCount(saved);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 此方法不被系统调用
        super.onDraw(canvas);
        Log.i(TAG, "onDraw: ");
    }

    /**
     * 绘制圆角
     * @param canvas
     */
    private void drawCorner(Canvas canvas) {
        rectF.set(0,0,getWidth(),getHeight());
        mTempPath.addRect(rectF, Path.Direction.CCW);
        mPath.addRoundRect(rectF, radii, Path.Direction.CCW);
        mPath.op(mTempPath, mPath, Path.Op.DIFFERENCE);
        canvas.drawPath(mPath, mPaint);
        mPaint.setXfermode(null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.i(TAG, "onSizeChanged, w: " + w + ", h: " + h);
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
            view.setGravity(Gravity.CENTER_HORIZONTAL| Gravity.CENTER_VERTICAL);
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
