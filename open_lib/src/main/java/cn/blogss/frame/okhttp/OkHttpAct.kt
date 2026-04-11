package cn.blogss.frame.okhttp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cn.blogss.frame.components.LogScreen
import cn.blogss.frame.vm.OkHttpViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun Body(viewModel: OkHttpViewModel = viewModel()){
    Column {
        Row {
            Button(onClick = {
                viewModel.getGirlImage()
            }) {
                Text("获取女神照片")
            }
        }
        // 当 imageUrl 不为空时，用 GlideImage 显示
        if(viewModel.girlImageUrl.isNotEmpty()) {
            GlideImage(
                model = viewModel.girlImageUrl,
                contentDescription = "女神照片",
                modifier = Modifier.size(300.dp)
            )
        }

        LogScreen()
    }
}

@Preview
@Composable
fun PreviewBody(){
    Body()
}
