package cn.blogss.helper.imageloader

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.ImageView
import androidx.collection.LruCache
import cn.blogss.helper.R
import com.jakewharton.disklrucache.DiskLruCache
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 * @创建人 560266
 * @文件描述 磁盘缓存的 curd 操作
 * @创建时间 2020/9/28
 */
class ImageLoader private constructor(context: Context) {
    private var mContext: Context = context.applicationContext
    private var mMemoryCache: LruCache<String,Bitmap>
    private var mDiskLruCache: DiskLruCache? = null
    private var mDiskCacheCreated = false
    private val mMainHandler = object: Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            val result = msg.obj as LoadResult
            val imageView = result.imageView
            val uri = imageView.getTag(TAG_KEY_URI).toString()
            if(uri == result.uri){// 防止 View 复用，导致列表错位问题
                imageView.setImageBitmap(result.bitmap)
            }else{
                Log.w(TAG, "handleMessage: set image bitmap,but url has be changed, ignored!")
            }
        }
    }

    init {
        // 实例化内存缓存
        val maxMemorySize = Runtime.getRuntime().maxMemory().toInt() / 1024 // KB
        val cacheSie = maxMemorySize / 8
        mMemoryCache = object: LruCache<String,Bitmap>(cacheSie){
            override fun sizeOf(key:String, bitMap:Bitmap): Int{
                return bitMap.rowBytes * bitMap.height / 1024   //一张图片的大小(KB)
            }
        }

        // 实例化磁盘缓存
        val cacheDirectory = getDiskCacheDir(mContext)
        Log.d(TAG, "cacheDirectory: $cacheDirectory")
        if(!cacheDirectory.exists()){
            cacheDirectory.mkdir()
        }
        // 判断磁盘的剩余容量是否小于缓存所需大小，即用户的手机空间不足，那就不创建磁盘缓存
        if(cacheDirectory.usableSpace > DISK_CACHE_MAX_SIZE){
            try {
                mDiskLruCache = DiskLruCache.open(cacheDirectory, APP_VERSION, VALUE_COUNT, DISK_CACHE_MAX_SIZE)
                mDiskCacheCreated = true
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        Log.d(TAG, "mDiskCacheCreated: $mDiskCacheCreated")
    }

    companion object {
        private const val TAG = "ImageLoader"
        private const val MESSAGE_POST_RESULT = 1

        /*磁盘缓存相关常量*/
        private val TAG_KEY_URI = R.id.imageloader_uri
        private const val DISK_CACHE_DIRECTORY_NAME = "bitmap"
        private const val APP_VERSION = 1
        private const val VALUE_COUNT = 1
        /*缓存总大小，当缓存总大小超过这个设定值后，DiskLruCache 会清除一些缓存从而保证总大小不大于这个设定值*/
        private const val DISK_CACHE_MAX_SIZE = 1024 * 1024 * 50L  // 50 M
        private const val DISK_CACHE_INDEX = 0

        /*线程池相关常量*/
        private val CPU_COUNT = Runtime.getRuntime().availableProcessors()  // 处理器核心数量
        private val CORE_POOL_SIZE = CPU_COUNT + 1  // 核心线程数
        private val MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1   // 最大线程数
        private const val KEEP_ALIVE = 10L  // 线程存活时间

        /**===============用于执行网络请求的线程池===============**/
        /*线程工厂*/
        private val sThreadFactory = object: ThreadFactory{
            private val mCount = AtomicInteger(1)
            override fun newThread(r: Runnable): Thread {
                return Thread(r, "ImageLoader#" + mCount.getAndIncrement())
            }

        }
        /*线程池*/
        private val THREAD_POOL_EXECUTOR = ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
                KEEP_ALIVE, TimeUnit.SECONDS, LinkedBlockingQueue(), sThreadFactory)

        /**
         * 单例模式，双重检查加锁
         * @param context Context
         * @return ImageLoader?
         */
        @Volatile private var mImageLoader:ImageLoader? = null
        fun getSingleton(context: Context): ImageLoader?{
            if(mImageLoader == null){
                synchronized(this){
                    if(mImageLoader == null){
                        mImageLoader = ImageLoader(context)
                    }
                }
            }
            return mImageLoader
        }
    }

    /**
     * 外部可以调用的方法，用于将图片绑定到控件上。如果本地缓存没有这张图，那么需要请求网络。
     * @param uri String，图片地址
     * @param imageView ImageView，需要加载图片的控件
     * @param targetWidth Int，控件的宽
     * @param targetHeight Int，控件的高
     */
    fun bindBitmap(uri: String, imageView: ImageView, targetWidth: Int, targetHeight: Int){
        imageView.setTag(TAG_KEY_URI,uri)
        val bitmap = loadBitmapFromCache(uri)
        if(bitmap != null){
            imageView.setImageBitmap(bitmap)
            return
        }

        /*异步加载，从磁盘还是网络加载都是耗时的，所以需要异步加载*/
        val loadBitmapTask = Runnable {
            val bitmap = loadBitmap(uri, targetWidth, targetHeight)
            if(bitmap != null){//   加载成功后需要在主线程更新 UI
                val loadResult = LoadResult(imageView,uri,bitmap)
                mMainHandler.obtainMessage(MESSAGE_POST_RESULT,loadResult).sendToTarget()
            }
        }
        THREAD_POOL_EXECUTOR.execute(loadBitmapTask)
    }

    data class LoadResult(var imageView: ImageView, var uri: String, var bitmap: Bitmap)

    /**
     * 从磁盘或者网络加载图片
     * 1. 磁盘有对应的图片，直接返回
     * 2. 磁盘没有，则从网络下载
     * 3. 将下载的图片存储到内存缓存和磁盘缓存
     * @param uri String
     * @param targetWidth Int
     * @param targetHeight Int
     * @return Bitmap
     */
    private fun loadBitmap(uri: String, targetWidth: Int, targetHeight: Int): Bitmap {
        var bitmap = loadBitmapFromCache(uri)
        if(bitmap != null){
            Log.d(TAG, "loadBitmap: loadBitmapFromCache,url: $uri")
            return bitmap
        }
        try {
            bitmap = loadBitmapFromDiskCache(uri, targetWidth, targetHeight)
            if(bitmap != null){
                Log.d(TAG, "loadBitmap: loadBitmapFromDiskCache,url: $uri")
                return bitmap
            }
            bitmap = loadBitmapFromHttp(uri,targetWidth,targetHeight)
            if(bitmap != null){
                Log.d(TAG, "loadBitmap: loadBitmapFromHttp,url: $uri")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun loadBitmapFromHttp(uri: String, targetWidth: Int, targetHeight: Int): Bitmap? {

    }

    /**
     * 1. 从磁盘缓存查询对应的 Bitmap
     * 2. 如果找到了，将其存储到内存缓存
     * @param uri String
     * @param targetWidth Int
     * @param targetHeight Int
     * @return Bitmap?
     */
    private fun loadBitmapFromDiskCache(uri: String, targetWidth: Int, targetHeight: Int): Bitmap? {
        if(Looper.myLooper() == Looper.getMainLooper()){
            Log.w(TAG, "loadBitmapFromDiskCache: load bitmap from UI Thread, it's not recommended!")
        }
        if(mDiskLruCache == null)
            return null
        var bitmap: Bitmap? = null
        val key = Util.hashKeyFromUrl(uri)
        val snapshot = mDiskLruCache!!.get(key)
        if(snapshot != null){
            val fins = snapshot.getInputStream(DISK_CACHE_INDEX) as FileInputStream
            val fs = fins.fd
            bitmap = resizeImageFromFileDes(fs,targetWidth,targetHeight)
        }
        if(bitmap != null){
            addBitmapToMemoryCache(key,bitmap)
        }
        return bitmap
    }

    /**
     * 根据 uri 从内存缓存查询对应的 Bitmap
     * @param uri String
     * @return Bitmap?
     */
    private fun loadBitmapFromCache(uri: String): Bitmap? {
        return mMemoryCache.get(Util.hashKeyFromUrl(uri))
    }

    /**
     * 添加 Bitmap 到内存缓存
     * @param key String
     * @param bitmap Bitmap
     */
    private fun addBitmapToMemoryCache(key: String, bitmap: Bitmap){
        if(getBitmapFromMemCache(key) == null){
            mMemoryCache.put(key, bitmap)
        }
    }

    /**
     * 根据 key 查询对应的 Bitmap
     * @param key String
     * @return Bitmap?
     */
    private fun getBitmapFromMemCache(key: String): Bitmap? {
        return mMemoryCache.get(key)
    }


    /**
     * 获取磁盘缓存目录
     * @param context Context
     * @return File
     */
    private fun getDiskCacheDir(context: Context): File{
        val externalStorageAvailable = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
        val cachePath:String
        cachePath = if(externalStorageAvailable){
            context.externalCacheDir!!.path
        }else{
            context.cacheDir.path
        }
        Log.d(TAG, "getDiskCacheDir: $cachePath")
        return File(cachePath+File.separator+ DISK_CACHE_DIRECTORY_NAME)
    }
}


