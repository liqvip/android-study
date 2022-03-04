package cn.blogss.core.view;

import android.view.LayoutInflater;

import androidx.lifecycle.ViewModel;

import cn.blogss.core.databinding.ActivityCanvasBinding;
import cn.blogss.helper.base.jetpack.BaseActivity;
import cn.blogss.helper.base.jetpack.BaseViewModel;

/**
 * Canvas
 */
public class CanvasActivity extends BaseActivity<ActivityCanvasBinding, BaseViewModel> {
    @Override
    protected BaseViewModel getViewModel() {
        return null;
    }

    @Override
    protected ActivityCanvasBinding getViewBinding(LayoutInflater inflater) {
        return ActivityCanvasBinding.inflate(inflater);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void bindObserver() {

    }

    @Override
    protected void initData() {

    }
}
