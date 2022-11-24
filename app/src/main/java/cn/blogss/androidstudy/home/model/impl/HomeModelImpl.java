package cn.blogss.androidstudy.home.model.impl;

import java.util.ArrayList;
import java.util.List;

import cn.blogss.androidstudy.R;
import cn.blogss.androidstudy.home.bean.HomeRvItemBean;
import cn.blogss.androidstudy.home.model.HomeModel;
import cn.blogss.helper.ResUtil;
import cn.blogss.helper.base.BaseApplication;

public class HomeModelImpl implements HomeModel {

    @Override
    public List<HomeRvItemBean> queryAllHomeRvData() {
        List<HomeRvItemBean> ls = new ArrayList<>();
        String[] strArr = ResUtil.getStrArr(BaseApplication.Companion.getContext(), R.array.home_rv_item_data);
        int[] itemIcons = ResUtil.getIds(BaseApplication.Companion.getContext(), ResUtil.getStrArr(BaseApplication.Companion.getContext(), R.array.home_rv_item_icon),"drawable");
        String[] intentActArr = ResUtil.getStrArr(BaseApplication.Companion.getContext(), R.array.home_rv_item_intent_act);
        for (int i=0; i<strArr.length; i++) {
            HomeRvItemBean homeRvItemBean = new HomeRvItemBean();
            homeRvItemBean.setName(strArr[i]);
            homeRvItemBean.setItemIcon(itemIcons[i%itemIcons.length]);
            homeRvItemBean.setActName(intentActArr[i]);
            ls.add(homeRvItemBean);
        }
        return ls;
    }
}
