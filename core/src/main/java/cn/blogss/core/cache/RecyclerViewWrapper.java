package cn.blogss.core.cache;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewWrapper extends RecyclerView {

    private LayoutListener mLayoutListener;

    public RecyclerViewWrapper(@NonNull Context context) {
        super(context);
    }

    public RecyclerViewWrapper(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewWrapper(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mLayoutListener != null) {
            mLayoutListener.onBeforeLayout();
        }
        super.onLayout(changed, l, t, r, b);

        if (mLayoutListener != null) {
            mLayoutListener.onAfterLayout();
        }
    }

    interface LayoutListener{
        void onBeforeLayout();
        void onAfterLayout();
    }

    public void setLayoutListener(LayoutListener layoutListener){
        mLayoutListener =layoutListener;
    }
}
