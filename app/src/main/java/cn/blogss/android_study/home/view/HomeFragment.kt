package cn.blogss.android_study.home.view

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import cn.blogss.android_study.R
import cn.blogss.android_study.home.bean.HomeRvItemBean
import cn.blogss.android_study.home.presenter.HomePresenter
import cn.blogss.core.base.BaseFragment
import cn.blogss.core.base.BaseRVAdapter
import cn.blogss.core.base.BaseRvHolder
import cn.blogss.core.listeners.OnItemClickListener
import cn.blogss.helper.dp2px

open class HomeFragment : BaseFragment() {
    private var homePresenter: HomePresenter? = null
    private var rvItems: RecyclerView? = null
    private var homeRvItemAdapter: BaseRVAdapter<HomeRvItemBean?>? = null
    override fun initView(rootView: View) {
        rvItems = findView(R.id.rv_items)
    }

    override fun initData() {
        homePresenter = HomePresenter(activity)
        homeRvItemAdapter = object : BaseRVAdapter<HomeRvItemBean?>(context, R.layout.home_rv_item, homePresenter!!.homeRvData) {
            override fun convert(holder: BaseRvHolder?, itemData: HomeRvItemBean?, position: Int) {
                val tvName = holder!!.getView<TextView>(R.id.tv_name)
                val ivImage = holder.getView<ImageView>(R.id.iv_image)
                val layoutParams = ivImage.layoutParams
                if ((position and 1) == 0)
                    layoutParams.height = dp2px(context!!, 150f)
                else
                    layoutParams.height = dp2px(context!!, 100f)
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
        rvItems!!.adapter = homeRvItemAdapter
        val layoutManager = StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL)
        rvItems!!.layoutManager = layoutManager
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }
}