package cn.blogss.helper.base.recyclerview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView 垂直分割线、水平分割线
 */
public class GridDividerItemDecoration extends RecyclerView.ItemDecoration {

    private final int mDividerHeight; // 分割线高度

    private final int margin; // 垂直分割线的上下 margin，水平分割线的上下 margin

    private final int spanCount;  // 一行的单元格个数

    private final Paint mPaint;

    private GridDividerItemDecoration(Build build) {
        this.mDividerHeight = build.mDividerHeight;
        this.margin = build.margin;

        if(build.spanCount <=0 ){
            throw new IllegalArgumentException("You must set spanCount greater than 0, now is " + build.spanCount);
        }else{
            this.spanCount = build.spanCount;
        }

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if(build.color != -1){
            mPaint.setColor(build.color);
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        // 第一列的 item 不需要垂直分割线
        if (parent.getChildAdapterPosition(view)%spanCount != 0) {
            outRect.left = mDividerHeight;
        }

        // 每个 item 底部都有分割线
        outRect.bottom = mDividerHeight;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();

        // 绘制垂直分割线
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int index = parent.getChildAdapterPosition(view);

            //第一列的 ItemView 不需要绘制
            if (index%spanCount == 0) {
                continue;
            }

            float dividerLeft = view.getLeft() - mDividerHeight;
            float dividerRight = view.getLeft();
            float dividerTop = view.getTop() + margin;
            float dividerBottom = view.getBottom() - margin;

            c.drawRect(dividerLeft, dividerTop, dividerRight, dividerBottom, mPaint);
        }

        // 绘制水平分割线
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);

            float dividerLeft = view.getLeft() + margin;
            float dividerRight = view.getRight() - margin;
            float dividerTop = view.getBottom();
            float dividerBottom = view.getBottom() + mDividerHeight;

            c.drawRect(dividerLeft, dividerTop, dividerRight, dividerBottom, mPaint);
        }
    }

    public static class Build{
        private int mDividerHeight;
        private int margin;
        private int color = -1;
        private int spanCount;

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

        public Build setSpanCount(int spanCount) {
            this.spanCount = spanCount;
            return this;
        }

        public GridDividerItemDecoration build(){
            return new GridDividerItemDecoration(this);
        }
    }
}
