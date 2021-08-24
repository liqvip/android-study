package cn.blogss.core.view.customview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

/**
 * As same as CustomToggleButton
 */
public class CustomSwitchCompat extends SwitchCompat {
    private OnCheckedChangeListener mListener;

    public CustomSwitchCompat(@NonNull Context context) {
        super(context);
    }

    public CustomSwitchCompat(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSwitchCompat(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setOnCheckedChangeListener(@Nullable OnCheckedChangeListener listener) {
        mListener = listener;
        super.setOnCheckedChangeListener(listener);
    }

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
