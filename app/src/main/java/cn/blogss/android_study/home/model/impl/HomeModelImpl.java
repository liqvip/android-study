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
        for (String s : strArr) {
            HomeRvItemBean homeRvItemBean = new HomeRvItemBean();
            homeRvItemBean.setName(s);
            ls.add(homeRvItemBean);
        }
        return ls;
    }
}
