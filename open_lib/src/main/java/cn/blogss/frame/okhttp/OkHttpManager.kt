package cn.blogss.frame.okhttp

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

class OkHttpManager private constructor(){
    /**
     * 默认的连接超时、读取超时、写入超时都为 10s
     */
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()

    companion object {
        val instance by lazy { OkHttpManager() }
    }


    fun get(request: Request, onResponse: (response: Response) -> Unit,
            onFailure: (e: IOException) -> Unit = { it.printStackTrace() }
        ){
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onFailure(e)
            }

            override fun onResponse(call: Call, response: Response) {
                onResponse(response)
            }
        })
    }
}