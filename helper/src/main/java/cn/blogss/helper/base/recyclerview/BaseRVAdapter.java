package cn.blogss.helper.base.recyclerview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * @创建人 560266
 * @文件描述
 * @创建时间 2020/4/28
 */
public abstract class BaseRVAdapter<T> extends RecyclerView.Adapter<BaseRvHolder> {
    private static final String TAG = "BaseRVAdapter";

    protected Context mContext;

    protected int mLayoutId;

    protected List<T> mData;

    protected LayoutInflater mInflater;

    private OnItemClickListener mOnItemClickListener;

    private int mViewHolderCount = 0;

    public BaseRVAdapter(Context context, int mLayoutId, List<T> mData) {
        this.mContext = context;
        this.mLayoutId = mLayoutId;
        this.mData = mData;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public BaseRvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mViewHolderCount++;
        Log.d(TAG, "onCreateViewHolder: ViewHolderCount: "+mViewHolderCount);
        BaseRvHolder holder = BaseRvHolder.get(mContext,null,parent,mLayoutId,-1);
        setItemClickListener(parent,holder,viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRvHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: "+position);
        holder.updatePosition(position);
        convert(holder,mData.get(position),position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 面向接口编程，点击事件可以由自己实现
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClickListener = onItemClickListener;
    }

    /**
     * 为每个item设置短按和长按事件
     * @param parent
     * @param holder
     * @param viewType
     */
    private void setItemClickListener(final ViewGroup parent, final BaseRvHolder holder, int viewType) {
        if(!enabled(viewType))
            return;
        View convertView = holder.getConvertView();
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClickListener != null){
                    int position = holder.getAdapterPosition();
                    mOnItemClickListener.onItemClick(parent,v,mData.get(position),position);
                }
            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(mOnItemClickListener != null){
                    int position = holder.getAdapterPosition();
                    return mOnItemClickListener.onItemLongClick(parent,v,mData.get(position),position);
                }
                return false;
            }
        });
    }

    private boolean enabled(int viewType) {
        return true;
    }

    /**
     * 对每个item进行数据绑定的方法，使用者必须实现这个方法
     * @param holder
     * @param itemData
     * @param position
     */
    protected abstract void convert(BaseRvHolder holder, T itemData, int position);
}
