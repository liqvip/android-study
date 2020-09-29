package cn.blogss.helper.imageloader

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.FileDescriptor

/**
 * @创建人 560266
 * @文件描述 Bitmap 的高效加载工具类，通过缩放图片的原始尺寸与要显示在如 ImageView 控件上大小保持接近，
 * 将缩小后的图片显示在控件上，就能降低内存占用从而在一定程度上避免 OOM
 * @创建时间 2020/9/27
 */

/**
 * 缩小图片尺寸
 * @param res Resources
 * @param resId Int
 * @param targetWidth Int, 控件的宽
 * @param targetHeight Int, 控件的高
 * @return Bitmap
 */
fun resizeImageFromRes(res: Resources, resId: Int, targetWidth: Int, targetHeight: Int): Bitmap{
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true // 此参数设置为 true, BitmapFactory 只会解析图片的原始宽/高信息, 并不会真正的加载图片
    BitmapFactory.decodeResource(res, resId, options)
    // 计算采样率
    options.inSampleSize = calculateInSampleSize(options, targetWidth, targetHeight)
    options.inJustDecodeBounds = false
    return BitmapFactory.decodeResource(res, resId, options)
}

/**
 *
 * @param fileDes FileDescriptor
 * @param targetWidth Int
 * @param targetHeight Int
 * @return Bitmap
 */
fun resizeImageFromFileDes(fileDes: FileDescriptor, targetWidth: Int, targetHeight: Int): Bitmap{
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true // 此参数设置为 true, BitmapFactory 只会解析图片的原始宽/高信息, 并不会真正的加载图片
    BitmapFactory.decodeFileDescriptor(fileDes, null, options)
    // 计算采样率
    options.inSampleSize = calculateInSampleSize(options, targetWidth, targetHeight)
    options.inJustDecodeBounds = false
    return BitmapFactory.decodeFileDescriptor(fileDes, null, options)
}

/**
 *计算采样率，采样率的值必须是 2 的幂，当然也可以不返回 2 的幂，系统会向下取整选择一个并选择一个最接近 2 的指数来代替
 * @param options Options
 * @param targetWidth Int
 * @param targetHeight Int
 * @return Int
 */
fun calculateInSampleSize(options: BitmapFactory.Options, targetWidth: Int, targetHeight: Int): Int {
    val width = options.outWidth
    val height = options.outHeight
    var inSampleSize = 1
    if(width > targetWidth || height > targetHeight){
        val halfWidth = width / 2
        val halfHeight = height / 2
        while((halfWidth/inSampleSize >= targetWidth) and (halfHeight/inSampleSize >= targetHeight)){
            inSampleSize = inSampleSize shl 1
        }
    }
    return inSampleSize
}
