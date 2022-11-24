package cn.blogss.androidstudy.home.view

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import cn.blogss.helper.dp2px

open class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    private var homeRvItemAdapter: BaseRVAdapter<HomeRvItemBean?>? = null

    override fun initData() {
        homeRvItemAdapter = object : BaseRVAdapter<HomeRvItemBean?>(context, R.layout.home_rv_item, viewModel.homeRvData) {
            override fun convert(holder: BaseRvHolder, itemData: HomeRvItemBean?, position: Int) {
                val tvName = holder.getView<TextView>(R.id.tv_name)
                val ivImage = holder.getView<ImageView>(R.id.iv_image)
                val layoutParams = ivImage.layoutParams
                if ((position and 1) == 0)
                    layoutParams.height = dp2px(context!!, 200f)
                else
                    layoutParams.height = dp2px(context!!, 150f)
                ivImage.layoutParams = layoutParams
                tvName.text = itemData!!.name
                ivImage.setImageResource(itemData.itemIcon)
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

        val layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        viewBinding.rvItems.adapter = homeRvItemAdapter
        viewBinding.rvItems.layoutManager = layoutManager
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