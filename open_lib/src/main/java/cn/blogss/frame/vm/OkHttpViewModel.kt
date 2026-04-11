package cn.blogss.frame.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import cn.blogss.frame.entity.GirlImageResponse
import cn.blogss.frame.okhttp.OkHttpManager
import cn.blogss.helper.LogRepository
import kotlinx.serialization.json.Json

class OkHttpViewModel: ViewModel() {
    var girlImageUrl by mutableStateOf("")

    private val http = OkHttpManager.instance

    fun getGirlImage(){
        val request = http.buildRequest("girl", "/api/img/tu/girl").build()
        http.get(request, {
            val body = it.body()?.string() ?: "响应为空"
            val result = Json.decodeFromString<GirlImageResponse>(body)
            girlImageUrl = result.url
            LogRepository.addLog(body)
        })
    }

    fun getWeather(){
        val request = http.buildRequest("weather", "/api/weather").build()
        http.get(request, {
            val body = it.body()?.string() ?: "响应为空"
            LogRepository.addLog("天气信息: $body")
        })
    }
}