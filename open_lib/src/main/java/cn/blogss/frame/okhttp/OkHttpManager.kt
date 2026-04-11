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

    val baseUrlInterceptor = BaseUrlInterceptor().apply {
        putBaseUrl("girl", "https://api.52vmy.cn")
        putBaseUrl("weather", "https://api.vvhan.com")
        setDefaultBaseUrl("https://api.52vmy.cn")
    }

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(baseUrlInterceptor)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()

    companion object {
        val instance by lazy { OkHttpManager() }
    }


    /**
     * 构建请求，业务层只需传服务名和相对路径，自动拼接 baseUrl 并标记 Service-Name。
     */
    fun buildRequest(serviceName: String, path: String): Request.Builder {
        val baseUrl = baseUrlInterceptor.getBaseUrl(serviceName)
        return Request.Builder()
            .url("$baseUrl$path")
            .header(BaseUrlInterceptor.HEADER_SERVICE_NAME, serviceName)
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