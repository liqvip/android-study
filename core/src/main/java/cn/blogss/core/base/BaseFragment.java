package cn.blogss.core.base;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.blogss.core.R;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {
    protected View rootView;

    public BaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(getLayoutId(), container, false);
            if (savedInstanceState != null) {
                onRestartInstance(savedInstanceState);
            }
            initView(rootView);
            initData();

            setDefaultClickable();
        }
        return rootView;
    }

    /**
     * 获取控件
     * @param viewId
     * @param <T>
     * @return
     */
    protected <T extends View> T findView(int viewId){
        return (T)rootView.findViewById(viewId);
    }

    /**
     * 防止点击穿透
     */
    protected void setDefaultClickable() {
        rootView.setClickable(true);
    }

    /**
     * 复原数据
     * @param bundle
     */
    protected void onRestartInstance(Bundle bundle) {
    }

    protected abstract void initView(View rootView);

    protected abstract void initData();

    protected abstract int getLayoutId();
}
