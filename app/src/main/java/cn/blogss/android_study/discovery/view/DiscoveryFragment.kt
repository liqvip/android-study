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

    }

    override fun bindObserver() {
    }

    override fun initData() {

    }

    override fun onClick(v: View) {

    }
}
