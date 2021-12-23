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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.blogss.core.R;


/**
 * 多个 TextView 组合控件
 */

public class TextViewGroup extends ViewGroup {
    private static final String TAG = "TextViewGroup";

    private Paint paint;
    private Path innerPath;
    private Path outerPath;
    private RectF rectF;

    private Xfermode xfermode;

    private int childWidth;
    private int childHeight;
    private int childCount;

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


    private void init(AttributeSet attrs) {
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        paint = new Paint();
        innerPath = new Path();
        outerPath = new Path();
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
            childCount++;
        }

        setRadii();
        typedArray.recycle();

        Log.i(TAG, "textViewCount: " + textViewCount);
        Log.i(TAG, "radius: " + radius);
        Log.i(TAG, "cornerPosition: " + cornerPosition);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        childWidth = measureWidth / childCount;
        childHeight = measureHeight;
        for (int i=0; i<childCount; i++){
            getChildAt(i).getLayoutParams().width = childWidth;
            getChildAt(i).getLayoutParams().height = childHeight;
        }

        measureChildren(widthMeasureSpec,heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft = 0;
        for (int i=0; i<childCount; i++){
            View childView = getChildAt(i);
            if(childView.getVisibility() == VISIBLE){
                childView.layout(childLeft,0,childLeft+childWidth, getMeasuredHeight());
                childLeft += childWidth;
            }
        }
    }


    @Override
    public void draw(Canvas canvas) {
        int saved = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        super.draw(canvas);
        drawCorner(canvas);
        canvas.restoreToCount(saved);
    }


    private void drawCorner(Canvas canvas) {
        paint.reset();
        paint.setStyle(Paint.Style.FILL);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(xfermode);
        rectF.set(0,0,getWidth(), getHeight());

        outerPath.reset();
        innerPath.reset();
        outerPath.addRect(rectF, Path.Direction.CCW);
        innerPath.addRoundRect(rectF, radii, Path.Direction.CCW);
        outerPath.op(innerPath, Path.Op.DIFFERENCE);
        canvas.drawPath(outerPath, paint);
        paint.setXfermode(null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.i(TAG, "w = " + w + ", height = " + h + ", oldw = " + oldw + ", oldh = " + oldh);
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
        if(textViewColor.length > childCount){
            throw new IllegalArgumentException("Max textViewColor.length is " + childCount + ", now is " + textViewColor.length);
        }
        for (int i=0; i<childCount; i++){
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
        if(childrenText.length > childCount){
            throw new IllegalArgumentException("Max childrenText.length is " + childCount + ", now is " + childrenText.length);
        }
        for (int i=0; i<childCount; i++){
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
        for (int i=0; i<childCount; i++){
            TextView view = (TextView) getChildAt(i);
            view.setTextSize(sp);
        }
    }

    /**
     * 设置子 View 文本颜色
     * @param childrenTextColor Color resource ID array.
     */
    public void setChildrenTextColor(int[] childrenTextColor){
        if(childrenTextColor.length > childCount){
            throw new IllegalArgumentException("TextViewCount is less than childrenTextColor.length!");
        }
        for (int i=0; i<childrenTextColor.length; i++){
            TextView view = (TextView) getChildAt(i);
            view.setTextColor(getContext().getResources().getColor(childrenTextColor[i]));
        }
    }


    public void setVisible(int index){
        if(index >= childCount){
            throw new IllegalArgumentException("Max index is " + (childCount-1) + ", now is " + index);
        }
        View view = getChildAt(index);
        if(view.getVisibility() == GONE){
            getChildAt(index).setVisibility(VISIBLE);
            requestLayout();
        }
    }

    public void setInvisible(int index){
        if(index >= childCount){
            throw new IllegalArgumentException("Max index is " + (childCount-1) + ", now is " + index);
        }
        View view = getChildAt(index);
        if(view.getVisibility() == VISIBLE){
            getChildAt(index).setVisibility(GONE);
            requestLayout();
        }
    }
}
