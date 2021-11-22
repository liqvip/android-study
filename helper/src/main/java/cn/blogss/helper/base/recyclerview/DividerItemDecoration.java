package cn.blogss.helper.base.recyclerview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView 底部水平分割线
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private int mDividerHeight; // 分割线高度

    private int margin; // 分割线左右 margin

    private int color;  // 分割线颜色

    private boolean ignoreParentPadding;

    private Paint mPaint;

    private DividerItemDecoration(Build build) {
        this.mDividerHeight = build.mDividerHeight;
        this.margin = build.margin;
        this.color = build.color;
        this.ignoreParentPadding = build.ignoreParentPadding;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(this.color);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //第一个ItemView不需要在上面绘制分割线
        if (parent.getChildAdapterPosition(view) != 0) {
            outRect.top = mDividerHeight;
        }
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int index = parent.getChildAdapterPosition(view);
            //第一个ItemView不需要绘制
            if (index == 0) {
                continue;
            }
            float dividerLeft = margin;
            float dividerRight = parent.getWidth() - margin;
            float dividerTop = view.getTop() - mDividerHeight;
            float dividerBottom = view.getTop();

            if(!ignoreParentPadding){
                dividerLeft = parent.getPaddingLeft() + margin;
                dividerRight = parent.getWidth() - parent.getPaddingRight() - margin;
            }

            c.drawRect(dividerLeft, dividerTop, dividerRight, dividerBottom, mPaint);
        }
    }

    public static class Build{
        private int mDividerHeight;
        private int margin;
        private int color;
        // 忽略 RecyclerView 设置的左右 padding，既分割线的长度占满 RecyclerView
        private boolean ignoreParentPadding;

        public Build setDividerHeight(int mDividerHeight) {
            this.mDividerHeight = mDividerHeight;
            return this;
        }

        public Build setMargin(int margin) {
            this.margin = margin;
            return this;
        }

        public Build setColor(int color) {
            this.color = color;
            return this;
        }

        public Build setIgnoreParentPadding(boolean ignore) {
            this.ignoreParentPadding = ignore;
            return this;
        }

        public DividerItemDecoration build(){
            return new DividerItemDecoration(this);
        }
    }
}
