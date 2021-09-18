package cn.blogss.core.view.customview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import cn.blogss.core.R;

/**
 * 旋转的 ImageView
 */
public class RotateImageView extends AppCompatImageView {
    private static final String TAG = "RotateImageView";

    private int duration = 1000;

    private int repeatCount  = ValueAnimator.INFINITE;

    private int repeatMode = ValueAnimator.RESTART;

    public RotateImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public RotateImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.RotateImageView,0,0);
        int attrCount = typedArray.getIndexCount();
        Log.i(TAG, "attrCount: " + attrCount);

        for (int i=0; i< attrCount; i++){
            int index = typedArray.getIndex(i);
            Log.i(TAG, "index: " + index);
            if(index == R.styleable.RotateImageView_duration){
                duration = typedArray.getInt(index,duration);
            }else if(index == R.styleable.RotateImageView_repeatCount){
                repeatCount = typedArray.getInt(index,repeatCount);
                if(repeatCount < -1){
                    throw new IllegalArgumentException("repeatCount can't less than -1.");
                }
            }else if(index == R.styleable.RotateImageView_repeatMode){
                repeatMode = typedArray.getInt(index,repeatMode);
            }
        }
        typedArray.recycle();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(this,"rotation",0.0f,360.0f);
        rotateAnimator.setDuration(duration);
        rotateAnimator.setRepeatCount(repeatCount);
        rotateAnimator.setRepeatMode(repeatMode);
        // 插值器，匀速旋转
        rotateAnimator.setInterpolator(null);
        rotateAnimator.start();   Log.i(TAG, "onAttachedToWindow");
    }
}
