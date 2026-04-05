package cn.blogss.core.service

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 *
 */
class StartServiceAct: AppCompatActivity() {
    val logSb = StringBuilder()
    
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
    Column {
        Row {
            Button(onClick = {
            }) {
                Text("启动 Service")
            }
            Button(onClick = {
            }) {
                Text("停止 Service")
            }
        }
        Row {
            Button(onClick = {
            }) {
                Text("绑定 Service")
            }
            Button(onClick = {
            }) {
                Text("解绑 Service")
            }
        }
        // 日志打印
        Text(
            text = "",
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxSize()
        )
    }
}

@Composable
@Preview
fun PreviewBody(){
    Body()
}

