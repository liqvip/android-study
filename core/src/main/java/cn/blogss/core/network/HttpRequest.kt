package cn.blogss.core.network

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * @创建人 560266
 * @文件描述 原生 Http 请求
 * @创建时间 2020/9/17
 */

const val READ_TIMEOUT = 8*1000
const val CONN_TIMEOUT = 8*1000
const val REQUEST_METHOD_GET = "GET"
const val REQUEST_METHOD_POST = "POST"

/**
 * 异步发送一个请求方式为 GET 的网络请求，使用 HttpURLConnection。
 * 从Android 4.4开始，HttpURLConnection的实现是通过调用 OkHttp 完成的
 */
fun asynGetReq(url: String, listener: OnRequestListener){
    Thread(object : Runnable {
        override fun run() {
            var conn: HttpURLConnection? = null
            var reader: BufferedReader? = null
            try {
                val realUrl = URL(url);
                conn = realUrl.openConnection() as HttpURLConnection
                conn.requestMethod = REQUEST_METHOD_GET
                conn.connectTimeout = CONN_TIMEOUT
                conn.readTimeout = READ_TIMEOUT
                val ins = conn.inputStream // 建立连接，阻塞获取输入流
                reader = BufferedReader(InputStreamReader(ins))
                val response = StringBuilder()
                var line:String?
                while (reader.readLine().also { line = it } != null)
                    response.append(line)
                listener.onOK(response.toString())
            } catch (e: Exception){

            } finally {
                reader?.close()
                conn?.disconnect()
            }
        }
    }).start()
}