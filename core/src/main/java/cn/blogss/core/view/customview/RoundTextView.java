package cn.blogss.core.view.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class RoundTextView extends AppCompatTextView {
    private static final String TAG = "RoundTextView";

    private Paint paint;
    private Path innerPath;
    private Path outerPath;
    private RectF rectF;
    private PorterDuffXfermode xfermode;


    // Four corners x and y radius.
    private final float[] radii = new float[]{30.0F, 30.0F, 30.0F, 30.0F, 30.0F, 30.0F, 30.0F, 30.0F};

    public RoundTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        paint = new Paint();
        innerPath = new Path();
        outerPath = new Path();
        rectF = new RectF();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        super.draw(canvas);    // 目标图像
        drawCorner(canvas);  // 源图像
    }

    private void drawCorner(Canvas canvas) {
        paint.reset();
        paint.setXfermode(xfermode);
        paint.setColor(Color.parseColor("blue"));
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);

        outerPath.reset();
        innerPath.reset();
        rectF.set(0,0, getWidth(), getHeight());
        outerPath.addRect(rectF, Path.Direction.CCW);
        innerPath.addRoundRect(rectF, radii, Path.Direction.CCW);
        outerPath.op(innerPath, Path.Op.DIFFERENCE);

        canvas.drawPath(outerPath, paint);
        canvas.restore();
        paint.setXfermode(null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.i(TAG, "w = " + w + ", height = " + h + ", oldw = " + oldw + ", oldh = " + oldh);
    }
}
