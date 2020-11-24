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
import cn.blogss.helper.imageloader.ImageLoader

class CacheActivity : BaseActivity() {
    private lateinit var gvPics: GridView

    private var pics = mutableListOf<String>()
    private var imageLoader = ImageLoader.getSingleton(this)

    companion object {
        private const val TAG = "CacheActivity"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_cache
    }

    override fun initView() {
        gvPics = findViewById(R.id.gv_pics)
        gvPics.adapter = object: CommonBaseAdapter<String>(this,pics, R.layout.cache_gridview_item){
            override fun convert(convertView: View?, itemData: String?, position: Int) {
                val squareImageView = convertView?.findViewById(R.id.siv_item) as SquareImageView
                imageLoader?.bindBitmap(itemData!!,squareImageView,50f,50f)
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