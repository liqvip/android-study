package cn.blogss.core.view;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import cn.blogss.core.R;
import cn.blogss.core.base.BaseActivity;
import cn.blogss.core.view.customview.TextViewGroup;

/**
 * @创建人 560266
 * @文件描述 View 事件体系
 * @创建时间 2020/5/12
 */
public class ViewActivity extends BaseActivity implements View.OnClickListener {

    private MyScrollView vScroller;

    private LinearLayout llAnimator;

    private LinearLayout llDelayed;

    private TextViewGroup textViewGroup;

    private int mClickNum;

    /*延时策略，实现弹性滑动，大约在1000ms将View的内容向右移动了400个像素*/
    private int mCount = 0;
    private static final int MESSAGE_SCROLL_TO = 1;
    private static final int FRAME_COUNT = 30;
    private static final int DELAYED_TIME = 33;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == MESSAGE_SCROLL_TO) {
                mCount++;
                if(mCount <= FRAME_COUNT){
                    float fractioin = mCount / (float) FRAME_COUNT;
                    int scrollX = (int) (fractioin * -400);
                    llDelayed.scrollTo(scrollX,0);
                    mHandler.sendEmptyMessageDelayed(MESSAGE_SCROLL_TO,DELAYED_TIME);
                }
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_view;
    }

    @Override
    protected void initView() {
        vScroller = findViewById(R.id.v_scroller);
        llAnimator = findViewById(R.id.ll_animator);
        llDelayed = findViewById(R.id.ll_delayed);
        textViewGroup = findViewById(R.id.tv_group);
        textViewGroup.setChildrenBackgroundColor(new int[]{R.color.darkorchid,R.color.yellow,R.color.pink,R.color.blue,R.color.sienna});
        textViewGroup.setChildrenText(new int[]{R.string.As_Already_Auth,R.string.As_Tem_Control,R.string.As_Already_Collected,R.string.As_Already_Collected});
        textViewGroup.setChildrenTextColor(new int[]{R.color.black});
        textViewGroup.setChildrenTextSize(20);

        vScroller.setOnClickListener(this);
        /*动画弹性滑动*/
        ObjectAnimator.ofFloat(llAnimator,"translationX",0,400).setDuration(2000).start();

        /*延时策略弹性滑动*/
        mHandler.sendEmptyMessageDelayed(MESSAGE_SCROLL_TO,DELAYED_TIME);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.v_scroller){/*Scroller 弹性滑动*/
            mClickNum++;
            if(mClickNum % 2 == 0){
                vScroller.smoothScrollTo(0,0);
            }else{
                vScroller.smoothScrollTo(-500,0);
            }
        }
    }
}
