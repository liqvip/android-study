package cn.blogss.core.view.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import cn.blogss.core.R;

/**
 * Canvas 绘图
 */
public class CanvasView extends View {
    private Paint mPaint;
    private RectF rectF;
    private Path path;
    private Shader linearGradientShader;
    private Shader radialGradientShader;
    private Shader bitmapShader;
    Bitmap bitmap;

    public CanvasView(Context context) {
        super(context);
    }

    public CanvasView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
        init();
    }

    public CanvasView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStrokeWidth(2);   // 描边宽度
        mPaint.setStyle(Paint.Style.FILL);  // 填充
        mPaint.setAntiAlias(true);  // 抗锯齿
        mPaint.setColor(Color.parseColor("green")); // 设置画笔颜色

        rectF = new RectF();
        path = new Path();
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dog);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int center = getWidth()/2;
        int left = center;
        int top = 0;
        int right = getWidth();
        int bottom = getHeight();

        int radius = 50;
        int width = 100;
        int ovalWidth = 150;
        int height = 100;

        /**
         * canvas.drawXXX
         */

        // 绘制区域底色
        //canvas.drawColor(Color.parseColor("red"));
        mPaint.setColor(Color.parseColor("black"));

        // 绘制一个实心圆
        canvas.drawCircle(center,top+radius,radius,mPaint);
        top += radius*2;

        // 绘制一个空心圆
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(center,top+radius,radius,mPaint);
        top += radius*2;

        // 绘制一个椭圆
        mPaint.setStyle(Paint.Style.FILL);
        rectF.set(left,top,left+ovalWidth,top+height);
        canvas.drawOval(rectF,mPaint);
        top += height;

        // 绘制一个空心椭圆
        mPaint.setStyle(Paint.Style.STROKE);
        rectF.set(left,top,left+ovalWidth,top+height);
        canvas.drawOval(rectF,mPaint);
        top += height;

        // 绘制矩形
        mPaint.setStyle(Paint.Style.FILL);
        rectF.set(left,top,left+width,top+height);
        canvas.drawRect(rectF,mPaint);
        top += height;

        // 绘制圆角矩形
        rectF.set(left,top,left+width,top+height);
        canvas.drawRoundRect(rectF,20,20,mPaint);
        top += height;

        // 画线
        canvas.drawLine(left,top,right,top+height,mPaint);
        top += height;

        // 画扇形
        rectF.set(left,top,left+ovalWidth,top+height);
        canvas.drawArc(rectF,-90,90,true,mPaint);
        top += height;

        // 画弧形
        rectF.set(left,top,left+ovalWidth,top+height);
        canvas.drawArc(rectF,0,90,false,mPaint);
        top += height;

        // 画弧线
        mPaint.setStyle(Paint.Style.STROKE);
        rectF.set(left,top,left+ovalWidth,top+height);
        canvas.drawArc(rectF,90,90,false,mPaint);
        top += height;

        // 画自定义图形(path)
        //1. 画子图形
        path.addCircle(center,top+50,50, Path.Direction.CW);
        path.addCircle(center+50,top+50,50, Path.Direction.CW);
        canvas.drawPath(path,mPaint);
        top += height;

        // 2.画直线或曲线
        path.moveTo(0,top);  // 设置图形起点
        path.lineTo(left,top+height);
        path.rLineTo(center,-height);
        path.close();
        canvas.drawPath(path,mPaint);
        top += height;

        /**
         * Paint 画笔部分
         */

        mPaint.setStyle(Paint.Style.FILL);
        // Shader 着色器
        linearGradientShader = new LinearGradient(center-50,top+50,center+50,top+50,
                Color.parseColor("#E91E63"),Color.parseColor("#2196F3"),Shader.TileMode.CLAMP);
        mPaint.setShader(linearGradientShader);
        canvas.drawCircle(center,top+50,50,mPaint);
        top += height;

        radialGradientShader = new RadialGradient(center,top+50,50,
                Color.parseColor("#E91E63"),Color.parseColor("#2196F3"),Shader.TileMode.CLAMP);
        mPaint.setShader(radialGradientShader);
        canvas.drawCircle(center,top+50,50,mPaint);
        top += height;

        bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP,Shader.TileMode.CLAMP);
        mPaint.setShader(bitmapShader);
        canvas.drawCircle(center,top+50,50,mPaint);
        top += height;



    }
}
