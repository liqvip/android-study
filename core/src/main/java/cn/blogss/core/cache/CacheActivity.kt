package cn.blogss.core.cache

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.GridView
import cn.blogss.core.R
import cn.blogss.core.base.BaseActivity
import cn.blogss.core.base.CommonBaseAdapter
import cn.blogss.core.view.customview.SquareImageView
import cn.blogss.helper.dp2px
import cn.blogss.helper.getScreenMetrics
import cn.blogss.helper.imageloader.ImageLoader

class CacheActivity : BaseActivity() {
    private lateinit var gvPics: GridView
    private var mImageWidth = 0.0f

    private var pics = mutableListOf(
            "http://b.hiphotos.baidu.com/zhidao/pic/item/a6efce1b9d16fdfafee0cfb5b68f8c5495ee7bd8.jpg",
            "http://img3.3lian.com/2013/c2/32/d/101.jpg",
            "http://pic25.nipic.com/20121210/7447430_172514301000_2.jpg",
            "http://img2.3lian.com/2014/c7/51/d/26.jpg",
            "http://img.blogss.cn/myBlog/20200901222241741.jpg",
            "http://img.blogss.cn/myBlog/20200712155248014.jpg",
            "http://img.blogss.cn/myBlog/20200901220926527.jpg")
    private var imageLoader: ImageLoader? = null

    companion object {
        private const val TAG = "CacheActivity"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_cache
    }

    override fun initView() {
        gvPics = findViewById(R.id.gv_pics)
        imageLoader = ImageLoader.getSingleton(this)
        val screenWidth = getScreenMetrics(this).widthPixels
        val space = dp2px(this,20f)
        mImageWidth = ((screenWidth-space)/3).toFloat()
        gvPics.adapter = object: CommonBaseAdapter<String>(this,pics, R.layout.cache_gridview_item){
            override fun convert(convertView: View?, itemData: String?, position: Int) {
                val squareImageView = convertView?.findViewById(R.id.siv_item) as SquareImageView
                imageLoader?.bindBitmap(itemData!!,squareImageView,mImageWidth,mImageWidth)
            }
        }
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

}