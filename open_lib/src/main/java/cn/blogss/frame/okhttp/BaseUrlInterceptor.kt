package cn.blogss.frame.okhttp

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 通过自定义请求头 "Service-Name" 区分不同服务，动态替换 baseUrl。
 *
 * 使用方式：在构建 Request 时添加 header:
 *   Request.Builder().header("Service-Name", "girl").url("/api/path")...
 *
 * 拦截器会根据 Service-Name 查找对应的 baseUrl，重写请求地址，
 * 然后移除该自定义 header，避免将其发送到服务端。
 */
class BaseUrlInterceptor : Interceptor {

    private val serviceBaseUrls = mutableMapOf<String, HttpUrl>()
    private var defaultBaseUrl: HttpUrl? = null

    fun putBaseUrl(serviceName: String, baseUrl: String): BaseUrlInterceptor {
        HttpUrl.parse(baseUrl)?.let {
            serviceBaseUrls[serviceName] = it
        }
        return this
    }

    fun setDefaultBaseUrl(baseUrl: String): BaseUrlInterceptor {
        defaultBaseUrl = HttpUrl.parse(baseUrl)
        return this
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val serviceName = originalRequest.header(HEADER_SERVICE_NAME)
            ?: return chain.proceed(originalRequest)

        val targetBaseUrl = serviceBaseUrls[serviceName] ?: defaultBaseUrl
            ?: return chain.proceed(originalRequest)

        val originalUrl = originalRequest.url()

        val newUrl = originalUrl.newBuilder()
            .scheme(targetBaseUrl.scheme())
            .host(targetBaseUrl.host())
            .port(targetBaseUrl.port())
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .removeHeader(HEADER_SERVICE_NAME)
            .build()

        return chain.proceed(newRequest)
    }

    fun getBaseUrl(serviceName: String): String {
        val url = serviceBaseUrls[serviceName] ?: defaultBaseUrl
        return url?.toString()?.trimEnd('/') ?: ""
    }

    companion object {
        const val HEADER_SERVICE_NAME = "Service-Name"
    }
}
