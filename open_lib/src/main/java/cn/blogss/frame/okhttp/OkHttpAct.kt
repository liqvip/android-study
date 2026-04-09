package cn.blogss.frame.okhttp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cn.blogss.frame.components.LogScreen
import cn.blogss.helper.LogRepository
import okhttp3.Request

private const val baseUrl = "https://api.52vmy.cn/api/img/tu/girl"

class OkHttpAct: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Body()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

@Composable
fun Body(){
    Column {
        Row {
            Button(onClick = {
                val request = Request.Builder()
                    .url(baseUrl)
                    .build()
                OkHttpManager.instance.get(request, {
                    val body = it.body()?.string() ?: "响应为空"
                    LogRepository.addLog(body)
                })
            }) {
                Text("获取女神照片")
            }
        }
        LogScreen()
    }
}

@Preview
@Composable
fun PreviewBody(){
    Body()
}
