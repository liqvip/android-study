package cn.blogss.core.view.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
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
 * A ViewGroup which composed by multiple TextView. It also supports round corners.
 */

public class TextViewGroup extends ViewGroup {
    private Paint paint;
    private Path innerPath;
    private Path outerPath;
    private RectF outerRectF;
    private RectF innerRectF;

    private Xfermode xfermode;

    private int childWidth;

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
        setWillNotDraw(false);
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        paint = new Paint();
        outerPath = new Path();
        innerPath = new Path();
        outerRectF = new RectF();
        innerRectF = new RectF();

        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.TextViewGroup,0,0);
        int attrCount = typedArray.getIndexCount();

        for(int i=0;i<attrCount;i++){
            int index = typedArray.getIndex(i);
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

        for (int i=0; i<textViewCount; i++){
            TextView view = new TextView(getContext());
            view.setTextColor(Color.parseColor("#FFFFFF"));
            addView(view);
        }

        setRadii();
        typedArray.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);

        if(textViewCount == 0){
            return;
        }

        childWidth = measureWidth / textViewCount;
        for (int i=0; i<textViewCount; i++){
            getChildAt(i).getLayoutParams().width = childWidth;
            getChildAt(i).getLayoutParams().height = measureHeight;
        }

        measureChildren(widthMeasureSpec,heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft = 0;
        for (int i=0; i<textViewCount; i++){
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
        int visibleCount = 0;
        for (int i=0; i<getChildCount(); i++){
            if(getChildAt(i).getVisibility() == VISIBLE){
                visibleCount++;
            }
        }
        if(visibleCount == 0){
            return;
        }
        paint.reset();
        paint.setStyle(Paint.Style.FILL);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(xfermode);
        outerRectF.set(0f,0f,getWidth(), getHeight());
        innerRectF.set(0f, 0f, getWidth()*1.0f/getChildCount()*visibleCount, getHeight());

        outerPath.reset();
        innerPath.reset();
        outerPath.addRect(outerRectF, Path.Direction.CCW);
        innerPath.addRoundRect(innerRectF, radii, Path.Direction.CCW);
        outerPath.op(innerPath, Path.Op.DIFFERENCE);
        canvas.drawPath(outerPath, paint);
        paint.setXfermode(null);
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
     * Set children view's background color.
     * @param resId Color resource ID array, such as R.color.xxx.
     */
    public void setChildrenBgColor(int[] resId) {
        if(resId.length > textViewCount){
            throw new IllegalArgumentException("Max textViewColor.length is " + textViewCount + ", now is " + resId.length);
        }
        for (int i=0; i<resId.length; i++){
            TextView view = (TextView) getChildAt(i);
            view.setBackgroundColor(resId[i]);
        }
    }

    /**
     * Set children view's background color.
     * @param argb Hex color，such as "#FF0000".
     */
    public void setChildrenBgColor(String[] argb) {
        if(argb.length > textViewCount){
            throw new IllegalArgumentException("Max textViewColor.length is " + textViewCount + ", now is " + argb.length);
        }
        for (int i=0; i<argb.length; i++){
            TextView view = (TextView) getChildAt(i);
            view.setBackgroundColor(Color.parseColor(argb[i]));
        }
    }

    /**
     * Set children view's text.
     * @param resId Children text resId array.
     */
    public void setChildrenText(int[] resId){
        if(resId.length > textViewCount){
            throw new IllegalArgumentException("Max childrenText.length is " + textViewCount + ", now is " + resId.length);
        }
        for (int i=0; i<resId.length; i++){
            TextView view = (TextView) getChildAt(i);
            view.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            view.setGravity(Gravity.CENTER_HORIZONTAL| Gravity.CENTER_VERTICAL);
            view.setText(resId[i]);
        }
    }

    /**
     * Set children view's text.
     * @param text Children text string array.
     */
    public void setChildrenText(String[] text){
        if(text.length > textViewCount){
            throw new IllegalArgumentException("Max childrenText.length is " + textViewCount + ", now is " + text.length);
        }
        for (int i=0; i<text.length; i++){
            TextView view = (TextView) getChildAt(i);
            view.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            view.setGravity(Gravity.CENTER_HORIZONTAL| Gravity.CENTER_VERTICAL);
            view.setText(text[i]);
        }
    }

    /**
     * Set children view's font color.
     * @param resId Color resId array.
     */
    public void setChildrenTextColor(int[] resId){
        if(resId.length > textViewCount){
            throw new IllegalArgumentException("TextViewCount is less than resId.length!");
        }
        for (int i=0; i<resId.length; i++){
            TextView view = (TextView) getChildAt(i);
            view.setTextColor(getContext().getResources().getColor(resId[i]));
        }
    }

    /**
     * Set children view's font color.
     * @param argb Hex color，such as "#FF0000".
     */
    public void setChildrenTextColor(String[] argb){
        if(argb.length > textViewCount){
            throw new IllegalArgumentException("TextViewCount is less than argb.length!");
        }
        for (int i=0; i<argb.length; i++){
            TextView view = (TextView) getChildAt(i);
            view.setTextColor(Color.parseColor(argb[i]));
        }
    }

    /**
     * Set children view's font size.
     * @param sp size unit
     */
    public void setChildrenTextSize(int sp){
        for (int i=0; i<textViewCount; i++){
            TextView view = (TextView) getChildAt(i);
            view.setTextSize(sp);
        }
    }

    public void setVisible(int index){
        if(index >= textViewCount){
            throw new IllegalArgumentException("Max index is " + (textViewCount-1) + ", now is " + index);
        }
        View view = getChildAt(index);
        if(view.getVisibility() == GONE){
            getChildAt(index).setVisibility(VISIBLE);
            requestLayout();
        }
    }

    public void setInvisible(int index){
        if(index >= textViewCount){
            throw new IllegalArgumentException("Max index is " + (textViewCount-1) + ", now is " + index);
        }
        View view = getChildAt(index);
        if(view.getVisibility() == VISIBLE){
            getChildAt(index).setVisibility(GONE);
            requestLayout();
        }
    }
}
