package cn.blogss.core.base.recyclerview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 多 item 布局
 */

public abstract class MultiTypeBaseRVAdapter<T> extends RecyclerView.Adapter<BaseRvHolder> {
    private static final String TAG = "MultiTypeBaseRVAdapter";

    protected Context mContext;

    protected Map<Integer, Integer> typeViewMap;

    protected List<T> mData;

    protected LayoutInflater mInflater;

    private OnItemClickListener mOnItemClickListener;

    private int mViewHolderCount = 0;

    public MultiTypeBaseRVAdapter(Context context, List<T> mData, HashMap<Integer, Integer> typeViewMap) {
        this.mContext = context;
        this.mData = mData;
        this.typeViewMap = typeViewMap;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        return setItemViewType(position);
    }

    abstract public int setItemViewType(int position);

    @NonNull
    @Override
    public BaseRvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mViewHolderCount++;
        Log.d(TAG, "onCreateViewHolder: ViewHolderCount: "+mViewHolderCount);
        BaseRvHolder holder = null;
        if(typeViewMap != null && typeViewMap.containsKey(viewType)){
            holder = BaseRvHolder.get(mContext,null,parent,typeViewMap.get(viewType),-1);
        }
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
     * 设置点击事件
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
     * item数据绑定
     * @param holder
     * @param itemData
     * @param position
     */
    protected abstract void convert(BaseRvHolder holder, T itemData, int position);
}
