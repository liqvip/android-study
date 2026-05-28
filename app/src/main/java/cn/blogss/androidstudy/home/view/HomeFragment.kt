package cn.blogss.androidstudy.home.view

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import cn.blogss.androidstudy.R
import cn.blogss.androidstudy.databinding.FragmentHomeBinding
import cn.blogss.androidstudy.home.bean.HomeRvItemBean
import cn.blogss.androidstudy.home.vm.HomeViewModel
import cn.blogss.helper.base.jetpack.BaseFragment
import cn.blogss.helper.base.recyclerview.BaseRVAdapter
import cn.blogss.helper.base.recyclerview.BaseRvHolder
import cn.blogss.helper.base.recyclerview.OnItemClickListener

open class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override fun initData() {

    }


    override fun getViewModel(): HomeViewModel {
        return ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun initView() {
    }

    override fun bindObserver() {
    }
}