package cn.blogss.android_study.discovery.view;

import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import cn.blogss.android_study.R
import cn.blogss.android_study.databinding.FragmentDiscoveryBinding
import cn.blogss.helper.base.jetpack.BaseFragment

/**
 * 发现页
 */
class DiscoveryFragment: BaseFragment<FragmentDiscoveryBinding, ViewModel>(), View.OnClickListener {
    override fun getViewModel(): ViewModel? {
        return null
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentDiscoveryBinding {
        return FragmentDiscoveryBinding.inflate(inflater,container,false)
    }

    override fun initView() {
        viewBinding.tvScroll.setOnClickListener(this)
        viewBinding.tvScroll.setOnTouchListener { v, event ->
            val left = viewBinding.tvScroll.left
            val top = viewBinding.tvScroll.top
            val right = viewBinding.tvScroll.right
            val bottom = viewBinding.tvScroll.bottom
            val scrollX = viewBinding.tvScroll.scrollX
            val scrollY = viewBinding.tvScroll.scrollY
            val translationX = viewBinding.tvScroll.translationX
            val translationY = viewBinding.tvScroll.translationY
            val eventX = event.x
            val eventY = event.y
            val rawX = event.rawX
            val rawY = event.rawY
            viewBinding.tvDes.text = "触摸坐标：\n(left,top,right,bottom) = ($left,$top,$right,$bottom)" +
                    "\n(scrollX,scrollY) = ($scrollX,$scrollY)\n(translationX,translationY) = ($translationX,$translationY)" +
                    "\n(eventX,eventY) = ($eventX,$eventY)\n(rawX,rawY) = ($rawX,$rawY)"
           false
        }
    }

    override fun bindObserver() {
    }

    override fun initData() {

        viewBinding.tvScroll.post {
            val left = viewBinding.tvScroll.left
            val top = viewBinding.tvScroll.top
            val right = viewBinding.tvScroll.right
            val bottom = viewBinding.tvScroll.bottom
            val scrollX = viewBinding.tvScroll.scrollX
            val scrollY = viewBinding.tvScroll.scrollY
            val translationX = viewBinding.tvScroll.translationX
            val translationY = viewBinding.tvScroll.translationY
            viewBinding.tvDes.text = "初始坐标：\n(left,top,right,bottom) = ($left,$top,$right,$bottom)" +
                    "\n(scrollX,scrollY) = ($scrollX,$scrollY)\n(translationX,translationY) = ($translationX,$translationY)"
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_scroll -> {
                //viewBinding.tvScroll.scrollTo(-100,0) // 内容滚动
                // View 平移
                /*val translationXAnim = ObjectAnimator.ofFloat(viewBinding.tvScroll,"translationX",0f,100f).setDuration(2*1000)
                translationXAnim.addUpdateListener { valueAnimator ->
                    if(valueAnimator.animatedFraction == 1.0f){
                        val left = viewBinding.tvScroll.left
                        val top = viewBinding.tvScroll.top
                        val right = viewBinding.tvScroll.right
                        val bottom = viewBinding.tvScroll.bottom
                        val scrollX = viewBinding.tvScroll.scrollX
                        val scrollY = viewBinding.tvScroll.scrollY
                        val translationX = viewBinding.tvScroll.translationX
                        val translationY = viewBinding.tvScroll.translationY
                        viewBinding.tvDes.text = "向右平移100px：\n(left,top,right,bottom) = ($left,$top,$right,$bottom)" +
                                "\n(scrollX,scrollY) = ($scrollX,$scrollY)\n(translationX,translationY) = ($translationX,$translationY)"
                    }
                }
                translationXAnim.start()*/
            }
        }
    }
}
