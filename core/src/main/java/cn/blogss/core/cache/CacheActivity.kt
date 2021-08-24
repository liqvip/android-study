package cn.blogss.core.cache

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import cn.blogss.core.R
import cn.blogss.core.base.BaseActivity
import cn.blogss.core.base.recyclerview.BaseRVAdapter
import cn.blogss.core.base.recyclerview.BaseRvHolder
import cn.blogss.core.base.CommonBaseAdapter
import cn.blogss.core.view.customview.SquareImageView
import cn.blogss.helper.dp2px
import cn.blogss.helper.getScreenMetrics
import cn.blogss.helper.imageloader.ImageLoader
import java.lang.reflect.Field
import java.util.ArrayList

class CacheActivity : BaseActivity(), AbsListView.OnScrollListener {
    private lateinit var gvPics: GridView
    private lateinit var rvPics: RecyclerViewWrapper

    private lateinit var mImageAdapter: CommonBaseAdapter<String>
    private lateinit var mImageRvAdapter: BaseRVAdapter<String>
    private lateinit var mLayoutManager: GridLayoutManager
    private lateinit var mDefaultPic: Drawable
    private var mIsGridViewIdle = true
    private var mImageWidth = 0.0f
    private var pics = mutableListOf(
            "http://b.hiphotos.baidu.com/zhidao/pic/item/a6efce1b9d16fdfafee0cfb5b68f8c5495ee7bd8.jpg",
            "http://img3.3lian.com/2013/c2/32/d/101.jpg",
            "http://pic25.nipic.com/20121210/7447430_172514301000_2.jpg",
            "http://img2.3lian.com/2014/c7/51/d/26.jpg",
            "http://img.blogss.cn/myBlog/20200901222241741.jpg",
            "http://img.blogss.cn/myBlog/20200712155248014.jpg",
            "http://img.blogss.cn/myBlog/20200901220926527.jpg",
            "http://img.blogss.cn/myBlog/20200712144536680.jpg",
            "http://img.blogss.cn/myBlog/20200804094040990.jpg",
            "http://img.blogss.cn/myBlog/20200716003330485.jpg",
    "http://n.sinaimg.cn/news/1_img/upload/cf3881ab/400/w600h600/20200214/fe37-ipmxpvz8275276.jpg",
    "http://n.sinaimg.cn/news/1_img/upload/cf3881ab/110/w1000h710/20200214/d3a7-ipmxpvz8262445.jpg",
    "http://n.sinaimg.cn/news/1_img/upload/cf3881ab/79/w1000h679/20200214/4876-ipmxpvz8262666.jpg",
    "http://n.sinaimg.cn/news/1_img/upload/cf3881ab/67/w1000h667/20200214/ed2b-ipmxpvz8262667.jpg",
    "http://n.sinaimg.cn/news/1_img/upload/cf3881ab/71/w1000h1471/20200214/6cf5-ipmxpvz8262572.jpg",
    "http://n.sinaimg.cn/news/1_img/upload/cf3881ab/67/w1000h667/20200214/437f-ipmxpvz8262712.jpg",
    "http://n.sinaimg.cn/news/1_img/upload/cf3881ab/67/w1000h667/20200214/f5de-ipmxpvz8268530.jpg",
    "http://n.sinaimg.cn/photo/4_img/upload/9b7b89c5/676/w690h786/20201125/d95f-kefmphe4138132.jpg",
    "http://n.sinaimg.cn/photo/4_img/upload/9b7b89c5/752/w690h862/20201125/fe0b-kefmphe4138143.jpg")
    private var imageLoader: ImageLoader? = null

    companion object {
        private const val TAG = "CacheActivity"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_cache
    }

    override fun initView() {
        gvPics = findViewById(R.id.gv_pics)
        rvPics = findViewById(R.id.rv_pics)

        gvPics.setOnScrollListener(this)
        rvPics.setLayoutListener(object: RecyclerViewWrapper.LayoutListener {
            override fun onAfterLayout() {
                Log.d(TAG, "onAfterLayout: ")
                showMessage(rvPics)
            }

            override fun onBeforeLayout() {
                Log.d(TAG, "\n\nonBeforeLayout: ")
                val mRecycler = Class.forName("androidx.recyclerview.widget.RecyclerView").getDeclaredField("mRecycler")
                mRecycler.isAccessible = true
                val mRecyclerValue = mRecycler.get(rvPics) as RecyclerView.Recycler
                val mRecyclerClassObj = Class.forName(mRecycler.type.name)

                val mAttachedScrap = mRecyclerClassObj.getDeclaredField("mAttachedScrap")
                mAttachedScrap.isAccessible = true
                mAttachedScrap.set(mRecyclerValue, ArrayListWrapper<RecyclerView.ViewHolder>())
                showMessage(rvPics)
            }

        })
        /*rvPics.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                // ignore
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    mIsGridViewIdle = true
                    mImageRvAdapter.notifyDataSetChanged()
                }else{
                    mIsGridViewIdle = false
                }
            }
        })*/
        mDefaultPic = resources.getDrawable(R.drawable.image_load_default,null)
        imageLoader = ImageLoader.getSingleton(this)
        val screenWidth = getScreenMetrics(this).widthPixels
        val space = dp2px(this,20f)
        mImageWidth = ((screenWidth-space)/2).toFloat()
        mImageAdapter = object: CommonBaseAdapter<String>(this,pics, R.layout.cache_gridview_item){
            override fun convert(convertView: View?, itemData: String?, position: Int) {
                val squareImageView = convertView?.findViewById(R.id.siv_item) as SquareImageView
                val tag = squareImageView.tag
                if(tag != null && tag.toString() != itemData){// 预设一张加载图片，防止 view 复用导致列表错位
                    squareImageView.setImageDrawable(mDefaultPic)
                }
                if(mIsGridViewIdle) {
                    squareImageView.tag = itemData
                    imageLoader?.bindBitmap(itemData!!, squareImageView, mImageWidth, mImageWidth)
                }
            }
        }
        mImageRvAdapter = object: BaseRVAdapter<String>(this,R.layout.cache_gridview_item,pics){
            override fun convert(holder: BaseRvHolder?, itemData: String?, position: Int) {
                val squareImageView = holder!!.getView(R.id.siv_item) as SquareImageView
                val tag = squareImageView.tag
                if(tag != null && tag.toString() != itemData){// 预设一张加载图片，防止 view 复用导致列表错位
                    squareImageView.setImageDrawable(mDefaultPic)
                }
                squareImageView.tag = itemData
                imageLoader?.bindBitmap(itemData!!, squareImageView, mImageWidth, mImageWidth)
            }
        }
        gvPics.adapter = mImageAdapter
        mLayoutManager = GridLayoutManager(this,2)
        rvPics.layoutManager = mLayoutManager
        rvPics.adapter = mImageRvAdapter
    }

    private fun showMessage(rvPics: RecyclerViewWrapper) {
        val recyclerViewClassObj = RecyclerView::class.java
        val mRecycler = recyclerViewClassObj.getDeclaredField("mRecycler")
        mRecycler.isAccessible = true
        val mRecyclerValue = mRecycler.get(rvPics) as RecyclerView.Recycler

        val mRecyclerClassObj  = Class.forName(mRecycler.type.name)

        // 一级缓存
        val mAttachedScrap = mRecyclerClassObj.getDeclaredField("mAttachedScrap")
        mAttachedScrap.isAccessible = true
        val mAttachedScrapValue = mAttachedScrap.get(mRecyclerValue) as ArrayListWrapper<RecyclerView.ViewHolder>

        // 默认二级缓存容量
        val mViewCacheMax = mRecyclerClassObj.getDeclaredField("mViewCacheMax")
        mViewCacheMax.isAccessible = true
        val mViewCacheMaxValue = mViewCacheMax.get(mRecyclerValue) as Int

        // 二级缓存
        val mCachedViews = mRecyclerClassObj.getDeclaredField("mCachedViews")
        mCachedViews.isAccessible = true
        val mCachedViewsValue = mCachedViews.get(mRecyclerValue) as ArrayList<RecyclerView.ViewHolder>

        //四级缓存
        val mRecyclerPool = mRecyclerClassObj.getDeclaredField("mRecyclerPool")
        mRecyclerPool.isAccessible = true
        val mRecyclerPoolValue = mRecyclerPool.get(mRecyclerValue) as RecycledViewPool

        printAttachedScrapValue(mAttachedScrapValue)
        printCachedViewsValue(mCachedViewsValue,mViewCacheMaxValue)
        printRecyclerPoolValue(mRecyclerPool,mRecyclerPoolValue)
    }

    private fun printRecyclerPoolValue(mRecyclerPool: Field, mRecyclerPoolValue: RecycledViewPool) {
        val mRecycledViewPoolClassObj = Class.forName(mRecyclerPool.type.name)

        val mScrap = mRecycledViewPoolClassObj.getDeclaredField("mScrap")
        mScrap.isAccessible = true
        val mScrapValue = mScrap.get(mRecyclerPoolValue) as SparseArray<*>

        Log.d(TAG, "四级缓存type个数：${mScrapValue.size()}个")

        val mScrapDataClassObj = Class.forName("androidx.recyclerview.widget.RecyclerView\$RecycledViewPool\$ScrapData")
        val mScrapHeap  = mScrapDataClassObj.getDeclaredField("mScrapHeap")
        val mMaxScrap = mScrapDataClassObj.getDeclaredField("mMaxScrap")
        mScrapHeap.isAccessible = true
        mMaxScrap.isAccessible = true
        for (index in 0 until mScrapValue.size()){
            val mScrapHeapValue = mScrapHeap.get(mScrapValue[index]) as ArrayList<RecyclerView.ViewHolder>
            val mMaxScrapValue = mMaxScrap.get(mScrapValue[index]) as Int
            Log.d(TAG, "Type: $index,最大容量: $mMaxScrapValue,四级缓存ViewHolder:")
            for (item in mScrapHeapValue){
                Log.d(TAG, "$item")
            }
        }
    }

    /**
     * 打印二级缓存的信息
     * @param mCachedViewsValue ArrayList<ViewHolder>
     * @param mViewCacheMaxValue Int
     */
    private fun printCachedViewsValue(mCachedViewsValue: ArrayList<RecyclerView.ViewHolder>, mViewCacheMaxValue: Int) {
        Log.d(TAG, "二级缓存容量：$mViewCacheMaxValue")
        Log.d(TAG, "二级级缓存ViewHolder:")
        for (item in mCachedViewsValue){
            Log.d(TAG, "$item")
        }
        Log.d(TAG, "=======================================================================================")
    }

    /**
     * 打印一级缓存的信息
     * @param mAttachedScrapValue ArrayList<ViewHolder>
     */
    private fun printAttachedScrapValue(mAttachedScrapValue: ArrayListWrapper<RecyclerView.ViewHolder>) {
        Log.d(TAG, "一级缓存容量：${mAttachedScrapValue.maxSize}")
        Log.d(TAG, "一级缓存ViewHolder:")
        for (item in mAttachedScrapValue){
            Log.d(TAG, "$item")
        }
        Log.d(TAG, "=======================================================================================")
    }

    private fun printPath() {
        Log.i(TAG, "Environment.getDataDirectory(): "+Environment.getDataDirectory())
        Log.i(TAG, "getFilesDir().getPath(): "+filesDir.path)
        Log.i(TAG, "getFilesDir().getAbsolutePath(): "+filesDir.absolutePath)
        Log.i(TAG, "getFilesDir().getName(): "+filesDir.name)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTag(TAG)
        super.onCreate(savedInstanceState)
    }

    override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
        // ignore
    }

    /**
     * 防止用户刻意的频繁上下滑动，产生过多的异步任务，造成一定程度的卡顿
     * 等列表停下来再加载图片
     * @param view AbsListView
     * @param scrollState Int
     */
    override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
        if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
            mIsGridViewIdle = true
            mImageAdapter.notifyDataSetChanged()
        }else{
            mIsGridViewIdle = false
        }
    }

    private inner class ImageAdapter(var context: Context): BaseAdapter() {
        private var mLayoutInflater:LayoutInflater? = null
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val viewHolder: ViewHolder
            val resView: View
            if(convertView == null){
                mLayoutInflater = LayoutInflater.from(context)
                resView = mLayoutInflater!!.inflate(R.layout.cache_gridview_item,parent,false)
                val squareImageView = resView.findViewById(R.id.siv_item) as SquareImageView
                viewHolder = ViewHolder(squareImageView)
                resView.tag = viewHolder
            }else{
                resView = convertView
                viewHolder = convertView.tag as ViewHolder
            }
            val squareImageView = viewHolder.imageView
            val tag = squareImageView.tag
            val uri = getItem(position)
            if(tag != null && tag.toString() != uri){// 预设一张加载图片，防止 view 复用导致列表错位
                viewHolder.imageView.setImageDrawable(mDefaultPic)
            }
            if(mIsGridViewIdle) {
                squareImageView.tag = uri
                imageLoader?.bindBitmap(getItem(position), squareImageView, mImageWidth, mImageWidth)
            }
            return resView
        }

        override fun getItem(position: Int): String {
            return pics[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return pics.size
        }
    }

    data class ViewHolder(var imageView: ImageView)
}