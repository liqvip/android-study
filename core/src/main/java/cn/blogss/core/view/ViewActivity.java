package cn.blogss.core.view;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import cn.blogss.core.R;
import cn.blogss.core.base.jetpack.BaseActivity;
import cn.blogss.core.databinding.ActivityViewBinding;

/**
 * 自定义 View
 */
public class ViewActivity extends BaseActivity<ActivityViewBinding, ViewModel> implements View.OnClickListener {

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
    protected ViewModel getViewModel() {
        return null;
    }

    @Override
    protected ActivityViewBinding getViewBinding(LayoutInflater inflater) {
        return ActivityViewBinding.inflate(inflater);
    }

    @Override
    protected void initView() {
        viewBinding.vScroller.setOnClickListener(this);
    }

    @Override
    protected void bindObserver() {

    }

    @Override
    protected void initData() {
        viewBinding.tvGroup.setChildrenBackgroundColor(new int[]{R.color.darkorchid,R.color.yellow,R.color.pink,R.color.blue,R.color.sienna});
        viewBinding.tvGroup.setChildrenText(new int[]{R.string.As_Already_Auth,R.string.As_Tem_Control,R.string.As_Already_Collected,R.string.As_Already_Collected});
        viewBinding.tvGroup.setChildrenTextColor(new int[]{R.color.black});
        viewBinding.tvGroup.setChildrenTextSize(20);

        /*动画弹性滑动*/
        ObjectAnimator.ofFloat(viewBinding.llAnimator,"translationX",0,400).setDuration(2000).start();

        /*延时策略弹性滑动*/
        mHandler.sendEmptyMessageDelayed(MESSAGE_SCROLL_TO,DELAYED_TIME);
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
}
