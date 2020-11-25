package cn.blogss.helper.imageloader

import java.lang.Exception
import java.lang.StringBuilder
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * @创建人 560266
 * @文件描述
 * @创建时间 2020/9/29
 */
class Util {
    companion object {
        /**
         * 图片 url 可能含有特殊字符, 将 url 用 md5 加密或直接使用 url hashcode 的值
         * @param url String, 图片的 url 地址
         * @return String
         */
        fun hashKeyFromUrl(url: String): String{
            val cacheKey: String
            cacheKey = try {
                val mDigest = MessageDigest.getInstance("MD5")
                mDigest.update(url.toByte())
                bytesToHexString(mDigest.digest())
            } catch (e: Exception) {
                url.hashCode().toString()
            }
            return cacheKey
        }

        /**
         * 整数十进制转化为十六进制表示, 因为参数是字节数组，每个元素占 1 Byte ，8 个 bit 位。用十六进制来表示只
         * 需要两位来表示 8 个 bit 位的长度。
         * @param bytes ByteArray, 字节数组
         * @return String，16进制表示的字符串，1 Byte 占 2 个长度。即返回字符串的长度是 bytes.size * 2。
         */
        private fun bytesToHexString(bytes: ByteArray): String {
            val sb = StringBuilder()
            for(index in bytes.indices){
                val hex = bytes[index].toString(16)
                if(hex.length  == 1){
                    sb.append("0")
                }
                sb.append(hex)
            }
            return sb.toString()
        }
    }
}