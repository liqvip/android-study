package cn.blogss.helper.base.recyclerview;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @创建人 560266
 * @文件描述
 * @创建时间 2020/4/28
 */
public class BaseRvHolder extends RecyclerView.ViewHolder {
    private Context mContext;

    private View mItemView;

    private int mLayoutId;

    private int mPosition;

    private SparseArray<View> mItemViews;

    public BaseRvHolder(Context context, View itemV, ViewGroup parent, int position) {
        super(itemV);
        mContext = context;
        mItemView = itemV;
        mItemViews = new SparseArray<>();
        mItemView.setTag(this);
    }


    public static BaseRvHolder get(Context context, View itemView, ViewGroup parent, int layoutId, int position) {
        if (itemView == null) {
            View itemV = LayoutInflater.from(context).inflate(layoutId, parent, false);
            BaseRvHolder holder = new BaseRvHolder(context, itemV, parent, position);
            holder.mLayoutId = layoutId;
            return holder;
        } else {
            BaseRvHolder holder = (BaseRvHolder) itemView.getTag();
            holder.mPosition = position;
            return holder;
        }
    }

    public void updatePosition(int position) {
        mPosition = position;
    }

    public View getConvertView() {
        return mItemView;
    }

    /**
     * 通过viewId获取View
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId){
        View view = mItemViews.get(viewId);
        if (view == null) {
            view = mItemView.findViewById(viewId);
            mItemViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     *
     * @return
     */
    public int getLayoutId() {
        return mLayoutId;
    }
}
