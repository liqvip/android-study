package cn.blogss.helper.base.jetpack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import cn.blogss.helper.R;
import cn.blogss.helper.databinding.BaseTitleFragmentBinding;

/**
 * 带标题栏的 Fragment 基类
 */
public abstract class BaseTitleFragment extends BaseFragment<BaseTitleFragmentBinding, ViewModel> implements View.OnClickListener{
    private View titleView;
    private View activityView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Add default or custom title
        LayoutInflater inflater = LayoutInflater.from(getContext());
        titleView = inflater.inflate(getTitleView(), null);
        activityView = inflater.inflate(getActivityView(), null);

        if(titleView != null){
            viewBinding.getRoot().addView(titleView);
        }

        if(activityView != null){
            viewBinding.getRoot().addView(activityView);
        }
    }

    @Override
    protected BaseTitleFragmentBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return BaseTitleFragmentBinding.inflate(inflater,container,false);
    }

    /**
     * @return R.layout.xxx
     */
    protected int getTitleView() {
        return R.layout.default_title1_fragment;
    }

    /**
     * @return R.layout.xxx
     */
    @NonNull
    protected abstract int getActivityView();

    @Override
    public void onClick(View v) {
        int id = v.getId();
        int titleView = getTitleView();

        if(titleView == R.layout.default_title1_fragment){

        }else if (titleView == R.layout.default_title2_fragment){

        }
    }
}
