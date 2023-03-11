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
    private var homeRvItemAdapter: BaseRVAdapter<HomeRvItemBean?>? = null

    override fun initData() {
        homeRvItemAdapter = object : BaseRVAdapter<HomeRvItemBean?>(context, R.layout.home_rv_item, viewModel.homeRvData) {
            override fun convert(holder: BaseRvHolder, itemData: HomeRvItemBean?, position: Int) {
                val btName = holder.getView<TextView>(R.id.tv_name)
                btName.text = itemData!!.name
            }
        }

        /*设置点击事件*/
        homeRvItemAdapter!!.setOnItemClickListener(object : OnItemClickListener<HomeRvItemBean?> {
            override fun onItemClick(parent: ViewGroup?, view: View?, itemData: HomeRvItemBean?, position: Int) {
                val intent = Intent()
                intent.setClassName(context!!, itemData!!.actName)
                startActivity(intent)
            }

            override fun onItemLongClick(parent: ViewGroup?, view: View?, itemData: HomeRvItemBean?, position: Int): Boolean {
                return false
            }
        })

        val layoutManager = StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL)
        viewBinding.rvItems.layoutManager = layoutManager
        viewBinding.rvItems.adapter = homeRvItemAdapter
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