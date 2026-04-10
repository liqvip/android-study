package cn.blogss.frame.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import cn.blogss.frame.entity.GirlImageResponse
import cn.blogss.frame.okhttp.OkHttpManager
import cn.blogss.helper.LogRepository
import kotlinx.serialization.json.Json
import okhttp3.Request

private const val baseUrl = "https://api.52vmy.cn/api/img/tu/girl"

class UiState {
    var girlImageUrl: String = ""
}

class OkHttpViewModel: ViewModel() {
    // by 左边的值由右边的代理对象返回
    // MutableState 有 getValue 和 setValue 扩展方法
    private val _uiState by mutableStateOf(UiState())
    val uiState = _uiState

    fun getGirlImage(){
        val request = Request.Builder()
            .url(baseUrl)
            .build()
        OkHttpManager.instance.get(request, {
            val body = it.body()?.string() ?: "响应为空"
            val result = Json.decodeFromString<GirlImageResponse>(body)
            _uiState.girlImageUrl = result.url
            LogRepository.addLog(body)
        })
    }
}