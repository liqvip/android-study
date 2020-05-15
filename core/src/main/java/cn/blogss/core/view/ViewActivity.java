package cn.blogss.core.view;

import android.view.View;

import cn.blogss.core.R;
import cn.blogss.core.base.BaseActivity;

/**
 * @创建人 560266
 * @文件描述 View 事件体系
 * @创建时间 2020/5/12
 */
public class ViewActivity extends BaseActivity implements View.OnClickListener {

    private MyScrollView vScroller;

    private int mClickNum;
    @Override
    public int getLayoutId() {
        return R.layout.activity_view;
    }

    @Override
    protected void initView() {
        vScroller = findViewById(R.id.v_scroller);
        vScroller.setOnClickListener(this);
    }




    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.v_scroller){
            mClickNum++;
            if(mClickNum % 2 == 0){
                vScroller.smoothScrollTo(0,0);
            }else{
                vScroller.smoothScrollTo(-500,0);
            }
        }
    }
}
