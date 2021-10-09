package cn.blogss.core.view.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 折线图
 */
public class LineChartView extends View {
    private Paint axesPaint;
    private Paint axesPointPaint;
    private int widthCriterion;
    private int heightCriterion;
    private int minCriterion;
    private int textFont;

    public LineChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
        init();
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        axesPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        axesPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        axesPaint.setStyle(Paint.Style.STROKE);
        axesPaint.setStrokeWidth(5.0f);

        axesPointPaint.setStrokeWidth(5.0f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        widthCriterion = getWidth() / 10;
        heightCriterion = getHeight() / 10;
        minCriterion = widthCriterion > heightCriterion ? heightCriterion/2 : widthCriterion/2;

        // 1. 画坐标系
        drawAxes(canvas);

        // 2.画坐标系上的点
        drawAxesPoint(canvas);
    }

    /**
     * 画坐标系
     * @param canvas 画布
     */
    private void drawAxes(Canvas canvas) {
        // y 轴
        canvas.drawLine(widthCriterion,heightCriterion,widthCriterion,heightCriterion*9,axesPaint);
        // y 角
        canvas.drawLine(widthCriterion-minCriterion,heightCriterion+minCriterion,widthCriterion,heightCriterion,axesPaint);
        canvas.drawLine(widthCriterion,heightCriterion,widthCriterion+minCriterion,heightCriterion+minCriterion,axesPaint);
        // x 轴
        canvas.drawLine(widthCriterion,heightCriterion*9,widthCriterion*9,heightCriterion*9,axesPaint);
        // x 角
        canvas.drawLine(widthCriterion*9-minCriterion,heightCriterion*9-minCriterion,widthCriterion*9,heightCriterion*9,axesPaint);
        canvas.drawLine(widthCriterion*9,heightCriterion*9,widthCriterion*9-minCriterion,heightCriterion*9+minCriterion,axesPaint);
    }

    /**
     * 画坐标系上的点
     * @param canvas 画布
     */
    private void drawAxesPoint(Canvas canvas) {
        textFont = widthCriterion/5*2;
        Typeface font = Typeface.create(Typeface.SANS_SERIF,Typeface.BOLD);
        axesPointPaint.setTypeface(font);
        axesPointPaint.setTextSize(textFont);

        // x 轴上的点
        for (int i=1; i<=8; i++){
            String text = String.valueOf(i-1);
            float textWidth = axesPointPaint.measureText(text); // 文本宽度
            canvas.drawText(text,i*widthCriterion-textWidth/2,heightCriterion*9+textFont,axesPointPaint);
        }

        // y 轴上的点
        for (int i=1; i<=7; i++){
            String text = String.valueOf(i);
            float textWidth = axesPointPaint.measureText(text); // 文本宽度
            canvas.drawText(text,widthCriterion-textFont,heightCriterion*9-i*heightCriterion+textWidth/2,axesPointPaint);
        }

    }
}




