package cn.blogss.android_study.home.model;

import java.util.List;

import cn.blogss.android_study.home.bean.HomeRvItemBean;

/**
 * @创建人 560266
 * @文件描述
 * @创建时间 2020/4/28
 */
public interface HomeModel {
    List<HomeRvItemBean> queryAllHomeRvData();
}
