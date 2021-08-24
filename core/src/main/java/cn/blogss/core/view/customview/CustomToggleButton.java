package cn.blogss.core.view.customview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatToggleButton;

/**
 * @Descripttion: 切换按钮状态，而不触发事件所做的封装
 */
public class CustomToggleButton extends AppCompatToggleButton {

    private OnCheckedChangeListener mListener;

    public CustomToggleButton(Context context) {
        super(context);
    }

    public CustomToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setOnCheckedChangeListener(@Nullable OnCheckedChangeListener listener) {
        mListener = listener;
        super.setOnCheckedChangeListener(listener);
    }

    /**
     * @param checked 按钮是否选中
     * @param alsoNotify 是否触发点击事件
     */
    public void setChecked(boolean checked, boolean alsoNotify) {
        if (!alsoNotify) {
            super.setOnCheckedChangeListener(null);
            super.setChecked(checked);
            super.setOnCheckedChangeListener(mListener);
            return;
        }
        super.setChecked(checked);
    }
}
