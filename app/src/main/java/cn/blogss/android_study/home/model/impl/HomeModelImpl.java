package cn.blogss.android_study.home.model.impl;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import cn.blogss.android_study.R;
import cn.blogss.android_study.home.bean.HomeRvItemBean;
import cn.blogss.android_study.home.model.HomeModel;
import cn.blogss.helper.ResUtil;

/**
 * @创建人 560266
 * @文件描述
 * @创建时间 2020/4/28
 */
public class HomeModelImpl implements HomeModel {
    private Context mContext;

    public HomeModelImpl(Context context) {
        mContext = context;
    }

    @Override
    public List<HomeRvItemBean> queryAllHomeRvData() {
        List<HomeRvItemBean> ls = new ArrayList<>();
        String[] strArr = ResUtil.getStrArr(mContext,R.array.home_rv_item_data);
        int[] itemIcons = ResUtil.getIds(mContext,ResUtil.getStrArr(mContext,R.array.home_rv_item_icon),"drawable");
        String[] intentActArr = ResUtil.getStrArr(mContext,R.array.home_rv_item_intent_act);
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
