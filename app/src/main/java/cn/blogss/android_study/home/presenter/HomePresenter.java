package cn.blogss.android_study.home.presenter;

import android.content.Context;

import java.util.List;

import cn.blogss.android_study.home.bean.HomeRvItemBean;
import cn.blogss.android_study.home.model.HomeModel;
import cn.blogss.android_study.home.model.impl.HomeModelImpl;

/**
 * @创建人 560266
 * @文件描述
 * @创建时间 2020/4/28
 */
public class HomePresenter {
    private HomeModel homeModel;

    private Context mContext;

    public HomePresenter(Context context) {
        this.mContext = context;
        homeModel = new HomeModelImpl(context);
    }

    /**
     * 获取首页的数据
     * @return
     */
    public List<HomeRvItemBean> getHomeRvData() {
        return homeModel.queryAllHomeRvData();
    }
}
