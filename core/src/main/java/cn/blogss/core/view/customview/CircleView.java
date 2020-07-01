package cn.blogss.core.view.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @创建人 560266
 * @文件描述 自定义的一个圆形 View，继承自 View
 * @创建时间 2020/6/30
 */
class CircleView extends View {
    private int mColor = Color.RED;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CircleView(Context context) {
        super(context);
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint.setColor(mColor);
    }

    /**
     * 1.对于直接继承View 的控件，如果不对wrap_content做特殊处理，那么使用wrap_content就相当于使用match_parent。
     * 2.直接继承自View和ViewGroup的控件，padding是默认无法生效的，需要自己处理。
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
