package cn.blogss.androidstudy.home.vm;

import java.util.List;

import cn.blogss.androidstudy.home.bean.HomeRvItemBean;
import cn.blogss.androidstudy.home.model.HomeModel;
import cn.blogss.androidstudy.home.model.impl.HomeModelImpl;
import cn.blogss.helper.base.jetpack.BaseViewModel;

public class HomeViewModel extends BaseViewModel {
    private final HomeModel homeModel;

    public HomeViewModel() {
        homeModel = new HomeModelImpl();
    }

    /**
     * 获取首页的数据
     * @return
     */
    public List<HomeRvItemBean> getHomeRvData() {
        return homeModel.queryAllHomeRvData();
    }
}
