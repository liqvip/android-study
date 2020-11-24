package cn.blogss.core.view.customview

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView


class SquareImageView: AppCompatImageView {
    constructor(context: Context?) : super(context)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}