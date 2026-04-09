package cn.blogss.frame.okhttp

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class OkHttpManager {
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()

    companion object {
        val instance by lazy { OkHttpManager() }
    }
}