package cn.blogss.android_study.home.view;

import android.content.Intent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cn.blogss.android_study.R;
import cn.blogss.android_study.home.bean.HomeRvItemBean;
import cn.blogss.android_study.home.presenter.HomePresenter;
import cn.blogss.core.base.BaseFragment;
import cn.blogss.core.base.BaseRVAdapter;
import cn.blogss.core.base.BaseRvHolder;
import cn.blogss.core.listeners.OnItemClickListener;

public class HomeFragment extends BaseFragment {
    private HomePresenter homePresenter;

    private RecyclerView rvItems;

    private BaseRVAdapter<HomeRvItemBean> homeRvItemAdapter;

    @Override
    protected void initView(View rootView) {
        rvItems = findView(R.id.rv_items);
    }

    @Override
    protected void initData() {
        homePresenter = new HomePresenter(getActivity());
        homeRvItemAdapter = new BaseRVAdapter<HomeRvItemBean>(getContext(),R.layout.home_rv_item,homePresenter.getHomeRvData()) {
            @Override
            protected void convert(BaseRvHolder holder, HomeRvItemBean itemData, int position) {
                TextView tvName = holder.getView(R.id.tv_name);
                tvName.setText(itemData.getName());
            }
        };

        /*设置点击事件*/
        homeRvItemAdapter.setOnItemClickListener(new OnItemClickListener<HomeRvItemBean>() {
            @Override
            public void onItemClick(ViewGroup parent, View view, HomeRvItemBean itemData, int position) {
                Intent intent = new Intent();
                intent.setClassName(getContext(),itemData.getActName());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, HomeRvItemBean itemData, int position) {
                return false;
            }
        });

        rvItems.setAdapter(homeRvItemAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }
}
