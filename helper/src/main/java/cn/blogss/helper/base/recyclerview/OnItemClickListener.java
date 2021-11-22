package cn.blogss.helper.base.recyclerview;

import android.view.View;
import android.view.ViewGroup;

/**
 * @创建人 560266
 * @文件描述 RecyclerView 的点击事件接口
 * @创建时间 2020/4/29
 */
public interface OnItemClickListener<T> {
    void onItemClick(ViewGroup parent, View view, T itemData, int position);
    boolean onItemLongClick(ViewGroup parent, View view, T itemData, int position);
}
