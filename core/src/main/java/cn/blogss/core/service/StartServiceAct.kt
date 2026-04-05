package cn.blogss.core.service

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

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
    }
}

@Composable
@Preview
fun PreviewBody(){
    Body()
}

