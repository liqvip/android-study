package cn.blogss.android_study.discovery.view;

import android.view.LayoutInflater
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
        viewBinding.tvGroup.setChildrenText(arrayOf("托儿索", "杰斯", "鳄鱼"))
        viewBinding.tvGroup.setChildrenBgColor(arrayOf("#4C6D99", "#39A977", "#FF9134"))
        viewBinding.tvGroup.setChildrenTextSize(30)

        viewBinding.btHide1.setOnClickListener(this)
        viewBinding.btHide2.setOnClickListener(this)
        viewBinding.btHide3.setOnClickListener(this)

        viewBinding.btShow1.setOnClickListener(this)
        viewBinding.btShow2.setOnClickListener(this)
        viewBinding.btShow3.setOnClickListener(this)
    }

    override fun bindObserver() {
    }

    override fun initData() {

    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.bt_hide1 -> {
                viewBinding.tvGroup.setInvisible(0)
            }
            R.id.bt_hide2 -> {
                viewBinding.tvGroup.setInvisible(1)
            }
            R.id.bt_hide3 -> {
                viewBinding.tvGroup.setInvisible(2)
            }
            R.id.bt_show1 -> {
                viewBinding.tvGroup.setVisible(0)
            }
            R.id.bt_show2 -> {
                viewBinding.tvGroup.setVisible(1)
            }
            R.id.bt_show3 -> {
                viewBinding.tvGroup.setVisible(2)
            }
        }
    }
}
