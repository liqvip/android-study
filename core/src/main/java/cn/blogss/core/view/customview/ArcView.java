package cn.blogss.core.view.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 角度逐渐增加的弧形
 */
public class ArcView extends View {

    private Paint paint;
    private RectF rectF;
    private final int angleInc = 2;
    private int sweepAngle = 0;


    public ArcView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
        init();
    }

    public ArcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2.0f);

        rectF = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        rectF.set(0,0,getWidth(),getHeight());
        canvas.drawArc(rectF,0,sweepAngle,true,paint);

        sweepAngle += angleInc;
        if(sweepAngle > 360){
            sweepAngle -= sweepAngle;
        }

        invalidate();
    }


}

