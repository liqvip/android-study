package cn.blogss.core.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

abstract public class CommonBaseAdapter<T> extends BaseAdapter {
    private Context mContext;
    private List<T> mData;
    private int mLayoutId;
    private LayoutInflater mLayoutInflater;

    public CommonBaseAdapter(Context mContext, List<T> mData, int mLayoutId) {
        this.mContext = mContext;
        this.mData = mData;
        this.mLayoutId = mLayoutId;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
            convertView = mLayoutInflater.inflate(mLayoutId,parent,false);
        convert(convertView,getItem(position),position);
        return convertView;
    }

    protected abstract void convert(View convertView, T itemData, int position);
}
