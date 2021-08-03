package cn.blogss.core.view.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.blogss.core.R;

/**
 * 多个 TextView 组合控件
 */

public class TextViewGroup extends ViewGroup {
    private static final String TAG = "TextViewGroup";

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final GradientDrawable gradientDrawable = new GradientDrawable();
    // Four corners x and y radius.
    private final float[] radii = {
            0,0,    // top-left
            0,0,    // top-right
            0,0,    // bottom-right
            0,0     // bottom-left
    };

    private int textViewCount = 0;
    private int radius = 0;
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
            } else if(index == R.styleable.TextViewGroup_corner_position){
                cornerPosition = typedArray.getInt(i,cornerPosition);
            }
        }

        for (int i=0;i<textViewCount;i++){
            TextView view = new TextView(getContext());
            addView(view);
        }

        setCornerRadius();
        typedArray.recycle();

        Log.i(TAG, "textViewCount: " + textViewCount);
        Log.i(TAG, "radius: " + radius);
        Log.i(TAG, "cornerPosition: " + cornerPosition);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        Log.i(TAG, "width: " + width);
        if(textViewCount == 0){
            width = height = 0;
        }else{
            for (int i=0;i<getChildCount();i++){
                getChildAt(i).getLayoutParams().width = width/3;
                getChildAt(i).getLayoutParams().height = height;
            }
            measureChildren(widthMeasureSpec,heightMeasureSpec);
        }

        setMeasuredDimension(width,height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int childLeft = 0;
        int childWidth = getMeasuredWidth() / childCount;
        Log.i(TAG, "childWidth: "+childWidth);

        for (int i=0;i<childCount;i++){
            View childView = getChildAt(i);
            if(childView.getVisibility() == VISIBLE){
                childView.layout(childLeft,0,childLeft+childWidth,childView.getMeasuredHeight());
                childLeft += childWidth;
            }
        }
    }

    private void setCornerRadius() {
        if(radius > 0){
            if(cornerPosition == -1){
                gradientDrawable.setCornerRadius(radius);
            }else{
                setRadii();
                gradientDrawable.setCornerRadii(radii);
            }
        }

        setBackground(gradientDrawable);
    }

    private void setRadii() {
        if(containPosition(TOP_LEFT)){  // top-left
            radii[0] = radius;
            radii[1] = radius;
        }
        if(containPosition(TOP_RIGHT)){  // top-right
            radii[2] = radius;
            radii[3] = radius;
        }
        if(containPosition(BOTTOM_RIGHT)){  // bottom-right
            radii[4] = radius;
            radii[5] = radius;
        }
        if(containPosition(BOTTOM_LEFT)){  // bottom-left
            radii[6] = radius;
            radii[7] = radius;
        }
    }

    private boolean containPosition(int position) {
        return (position & cornerPosition) == position;
    }

    public void setTextViewColor(int[] textViewColor) {
        for (int i=0;i<textViewCount;i++){
            View view = getChildAt(i);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(textViewColor[i]);
            view.setBackground(drawable);
        }
    }

    public void setInVisibleView(int index){
        for (int i=0;i<textViewCount;i++){
            if(i == index){
                getChildAt(i).setVisibility(GONE);
            }else{
                getChildAt(i).setVisibility(VISIBLE);
            }
        }
    }
}
