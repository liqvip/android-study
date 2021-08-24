package cn.blogss.core.view.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import cn.blogss.core.R;


/**
 * xml 动态设置 TextView 背景颜色+圆角，避免写重复的 drawable 文件
 */
public class CornerTextView extends AppCompatTextView {
    private static final String TAG = "CornerTextView";

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final GradientDrawable gradientDrawable = new GradientDrawable();
    // Four corners x and y radius.
    private final float[] radii = {
            0,0,    // top-left
            0,0,    // top-right
            0,0,    // bottom-right
            0,0     // bottom-left
    };

    private int mBackgroundColor = Color.WHITE;
    private int radius = 0;
    private int cornerPosition = 0;
    public static final int TOP_LEFT = 1;
    public static final int TOP_RIGHT = 2;
    public static final int BOTTOM_RIGHT = 4;
    public static final int BOTTOM_LEFT = 8;

    public CornerTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public CornerTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.CornerTextView,0,0);
        int attrCount = typedArray.getIndexCount();
        Log.i(TAG, "attrCount: " + attrCount);

        for(int i=0;i<attrCount;i++){
            int index = typedArray.getIndex(i);
            Log.i(TAG, "index: " + index);
            if(index == R.styleable.CornerTextView_cornerTv_background_color){
                mBackgroundColor = typedArray.getColor(index,mBackgroundColor);
            } else if(index == R.styleable.CornerTextView_cornerTv_radius){
                radius = typedArray.getDimensionPixelSize(index,radius);
            } else if(index == R.styleable.CornerTextView_cornerTv_position){
                cornerPosition = typedArray.getInt(index,cornerPosition);
            }
        }

        typedArray.recycle();
        setColorAndRadius();

        Log.i(TAG, "backgroundColor: " + Integer.toHexString(mBackgroundColor));
        Log.i(TAG, "radius: " + radius);
        Log.i(TAG, "cornerPosition: " + cornerPosition);
    }

    private void setColorAndRadius() {
        gradientDrawable.setColor(mBackgroundColor);

        if(cornerPosition == 0){   // 没有指定圆角位置，默认四个圆角
            gradientDrawable.setCornerRadius(radius);
        }else{
            gradientDrawable.setCornerRadii(radii);
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

    /**
     * 动态设置背景颜色
     * @param argb
     */
    public void setBackgroundColor(int argb){
        mBackgroundColor = argb;
        setColorAndRadius();
    }

    /**
     * 动态设置圆角半径
     * @param dp
     */
    public void setRadius(int dp){
        cornerPosition = 0;
        radius = dp2px(getContext(),dp);
        setColorAndRadius();
    }

    public void setTopLeftRadius(int dp){
        cornerPosition = cornerPosition | TOP_LEFT;
        dp = dp2px(getContext(),dp);
        radii[0] = dp;
        radii[1] = dp;
        setColorAndRadius();
    }

    public void setTopRightRadius(int dp){
        cornerPosition = cornerPosition | TOP_RIGHT;
        dp = dp2px(getContext(),dp);
        radii[2] = dp;
        radii[3] = dp;
        setColorAndRadius();
    }

    public void setBottomRightRadius(int dp){
        cornerPosition = cornerPosition | BOTTOM_RIGHT;
        dp = dp2px(getContext(),dp);
        radii[4] = dp;
        radii[5] = dp;
        setColorAndRadius();
    }

    public void setBottomLeftRadius(int dp){
        cornerPosition = cornerPosition | BOTTOM_LEFT;
        dp = dp2px(getContext(),dp);
        radii[6] = dp;
        radii[7] = dp;
        setColorAndRadius();
    }

    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
