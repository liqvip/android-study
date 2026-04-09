package cn.blogss.frame.okhttp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cn.blogss.frame.components.LogScreen

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
        LogScreen()
    }
}

@Preview
@Composable
fun PreviewBody(){
    Body()
}
