package cn.blogss.helper.base.jetpack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import cn.blogss.helper.R;
import cn.blogss.helper.databinding.BaseTitleActivityBinding;

/**
 * 带标题栏的 Activity 基类
 */

public abstract class BaseTitleActivity extends BaseActivity<BaseTitleActivityBinding, BaseViewModel> implements View.OnClickListener {
    private View titleView;
    private View activityView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add default or custom title
        LayoutInflater inflater = LayoutInflater.from(this);
        titleView = inflater.inflate(getTitleView(), null);
        activityView = inflater.inflate(getActivityView(), null);

        if(titleView != null){
            viewBinding.getRoot().addView(titleView);
        }

        if(activityView != null){
            viewBinding.getRoot().addView(activityView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
        }

        initTitleView();
        initActivityView();
        bindActivityObserver();
        initActivityData();
    }

    protected void initTitleView() {
    }

    @Override
    protected BaseTitleActivityBinding getViewBinding(LayoutInflater inflater) {
        return BaseTitleActivityBinding.inflate(inflater);
    }

    /**
     * @return R.layout.xxx
     */
    protected int getTitleView() {
        return R.layout.default_title1_activity;
    }

    /**
     * @return R.layout.xxx
     */
    @NonNull
    protected abstract int getActivityView();

    protected abstract void initActivityView();

    protected abstract void bindActivityObserver();

    protected abstract void initActivityData();

    @Override
    protected void initView() {

    }

    @Override
    protected void bindObserver() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        int titleView = getTitleView();

        if(titleView == R.layout.default_title1_activity){

        }else if (titleView == R.layout.default_title2_activity){

        }
    }
}

