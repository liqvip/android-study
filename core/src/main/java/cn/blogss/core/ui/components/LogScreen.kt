package cn.blogss.core.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.blogss.helper.LogRepository


@Composable
fun LogScreen() {
    val logs by LogRepository.logs.collectAsState()
    val listState = rememberLazyListState()

    // 新日志到来时自动滚动到底部
    LaunchedEffect(logs.size) {
        if (logs.isNotEmpty()) {
            listState.animateScrollToItem(logs.size - 1)
        }
    }

    LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
        itemsIndexed(logs) { index, log ->
            Text(
                text = log,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp
            )
        }
    }
}