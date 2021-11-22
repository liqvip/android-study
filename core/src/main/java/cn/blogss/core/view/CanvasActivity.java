package cn.blogss.core.view;

import android.view.LayoutInflater;

import androidx.lifecycle.ViewModel;

import cn.blogss.core.databinding.ActivityCanvasBinding;
import cn.blogss.helper.base.jetpack.BaseActivity;

/**
 * Canvas
 */
public class CanvasActivity extends BaseActivity<ActivityCanvasBinding, ViewModel> {
    @Override
    protected ViewModel getViewModel() {
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
