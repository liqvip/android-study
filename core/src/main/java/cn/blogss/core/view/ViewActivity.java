package cn.blogss.core.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import cn.blogss.core.R;
import cn.blogss.core.databinding.ActivityViewBinding;
import cn.blogss.helper.base.jetpack.BaseActivity;
import cn.blogss.helper.base.jetpack.BaseViewModel;

/**
 * 自定义 View
 */
public class ViewActivity extends BaseActivity<ActivityViewBinding, BaseViewModel> implements
        View.OnClickListener, View.OnTouchListener {

    private int mClickNum;

    /*延时策略，实现弹性滑动，大约在1000ms将View的内容向右移动了400个像素*/
    private int mCount = 0;
    private static final int MESSAGE_SCROLL_TO = 1;
    private static final int FRAME_COUNT = 30;
    private static final int DELAYED_TIME = 33;
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == MESSAGE_SCROLL_TO) {
                mCount++;
                if(mCount <= FRAME_COUNT){
                    float fractioin = mCount / (float) FRAME_COUNT;
                    int scrollX = (int) (fractioin * -400);
                    viewBinding.llDelayed.scrollTo(scrollX,0);
                    mHandler.sendEmptyMessageDelayed(MESSAGE_SCROLL_TO,DELAYED_TIME);
                }
            }
        }
    };

    @Override
    protected BaseViewModel getViewModel() {
        return null;
    }

    @Override
    protected ActivityViewBinding getViewBinding(LayoutInflater inflater) {
        return ActivityViewBinding.inflate(inflater);
    }

    @Override
    protected void initView() {
        viewBinding.vScroller.setOnClickListener(this);
        viewBinding.vDrag.setOnTouchListener(this);
    }

    @Override
    protected void bindObserver() {

    }

    @Override
    protected void initData() {
        viewBinding.tvGroup.setChildrenBgColor(new int[]{R.color.darkorchid,R.color.yellow,R.color.pink,R.color.blue,R.color.sienna});
        viewBinding.tvGroup.setChildrenText(new int[]{R.string.As_Already_Auth,R.string.As_Tem_Control,R.string.As_Already_Collected,R.string.As_Already_Collected});
        viewBinding.tvGroup.setChildrenTextColor(new int[]{R.color.black});
        viewBinding.tvGroup.setChildrenTextSize(20);

        /*延时策略弹性滑动*/
        mHandler.sendEmptyMessageDelayed(MESSAGE_SCROLL_TO,DELAYED_TIME);

        /*属性动画、平移*/
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(viewBinding.llTranslationAnimator,"translationX",500,0).setDuration(20*1000);
        translationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                float translationX = (float)animation.getAnimatedValue("translationX");
                viewBinding.tvTranslationAnimator.setText("属性动画，平移\n动画完成度: " + fraction + "\n动画值: "+translationX);
            }
        });
        translationAnimator.setRepeatCount(ValueAnimator.INFINITE);
        translationAnimator.start();

        /*属性动画、缩放*/
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(viewBinding.llScaleAnimator,"scaleX",1.0f,0.5f).setDuration(20*1000);
        scaleXAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                float scaleX = (float)animation.getAnimatedValue("scaleX");
                viewBinding.tvScaleAnimator.setText("属性动画，缩放\n动画完成度: " + fraction + "\n动画值: "+scaleX);
            }
        });
        scaleXAnimator.setRepeatCount(ValueAnimator.INFINITE);
        scaleXAnimator.start();

        /*属性动画、旋转*/
        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(viewBinding.llRotateAnimator,"rotation",0.0f,360.0f).setDuration(20*1000);
        rotationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                float rotation = (float)animation.getAnimatedValue("rotation");
                viewBinding.tvRotateAnimator.setText("属性动画，旋转\n动画完成度: " + fraction + "\n动画值: "+rotation);
            }
        });
        rotationAnimator.setRepeatCount(ValueAnimator.INFINITE);
        rotationAnimator.start();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.v_scroller){/*Scroller 弹性滑动*/
            mClickNum++;
            if(mClickNum % 2 == 0){
                viewBinding.vScroller.smoothScrollTo(0,0);
            }else{
                viewBinding.vScroller.smoothScrollTo(-500,0);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v == viewBinding.vDrag){
            // 原始坐标
            int left  = v.getLeft();
            int top = v.getTop();
            int right = v.getRight();
            int bottom = v.getBottom();

            // 平移坐标
            float x = (int)v.getX();
            float y = (int)v.getY();
            float translationX = (int)v.getTranslationX();
            float translationY = (int)v.getTranslationY();

            // 触摸坐标
            float rawX = (int)event.getRawX();
            float rawY = (int)event.getRawY();
            float eventX = (int)event.getX();
            float eventY = (int)event.getY();

            viewBinding.tvDrag.setText("原始坐标：(left,top,right,bottom)=("+left+","+top+","+right+","+bottom+")\n" +
                    "平移坐标：(x,y,translationX,translationX)=("+x+","+y+","+translationX+","+translationY+")\n" +
                    "触摸坐标：(rawX,rawY,eventX,eventY)=("+rawX+","+rawY+","+eventX+","+eventY+")");
        }
        return false;
    }
}
