package cn.blogss.core.service

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import cn.blogss.core.ui.components.LogScreen

/**
 *
 */
class StartServiceAct: AppCompatActivity() {

    companion object {
        private const val TAG = "StartServiceAct"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Body()
        }
    }
}

@Composable
fun Body(){
    val context = LocalContext.current
    Column {
        Row {
            Button(onClick = {
                val intent = Intent(context, StartService::class.java)
                context.startService(intent)
            }) {
                Text("启动 Service")
            }
            Button(onClick = {
                val intent = Intent(context, StartService::class.java)
                context.stopService(intent)
            }) {
                Text("停止 Service")
            }
        }
        // 日志打印
        LogScreen()
    }
}

@Composable
@Preview
fun PreviewBody(){
    Body()
}

