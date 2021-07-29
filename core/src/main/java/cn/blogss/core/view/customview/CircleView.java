package cn.blogss.core.view.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import cn.blogss.core.R;

/**
 * @创建人 560266
 * @文件描述 自定义的一个圆形 View，继承自 View
 * @创建时间 2020/6/30
 */
public class CircleView extends View {
    private int mWidth = 100;

    private int mHeight = 100;

    private int mColor = Color.RED;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CircleView(Context context) {
        super(context);
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /*解析自定义属性的值并做相应处理*/
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        mColor = a.getColor(R.styleable.CircleView_circle_color, Color.RED);
        a.recycle();
        init();
    }

    private void init() {
        mPaint.setColor(mColor);
    }

    /**
     * 1.对于直接继承View 的控件，如果不对wrap_content做特殊处理，那么使用wrap_content就相当于使用match_parent。
     * 在这里，我们需要重写 onMeasure 方法并设置 wrap_content 时的自身大小，给View指定一个默认的内部宽/高(mWidth,mHeight)，
     * 对于非wrap_content情形，我们沿用系统的测量值即可。对于这个默认的内部宽/高如何指定，这个没有固定的依据，根据需要灵活指定即可。
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if(widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(mWidth, mHeight);
        }else if(widthSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(mWidth, heightSpecSize);
        }else if(heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(widthSpecSize, mHeight);
        }
    }
    /**
     * 1.直接继承自View和ViewGroup的控件，padding是默认无法生效的，需要自己处理。
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*加入了处理padding的逻辑，圆心和半径都会考虑到View四周的padding，从而做相应的调整*/
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();
        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;
        int radius = Math.min(width, height) / 2;/*求得圆的半径*/
        canvas.drawCircle(paddingLeft + width / 2, paddingTop + height / 2, radius, mPaint);
    }
}
