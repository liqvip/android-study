package cn.blogss.core.service

import android.app.IntentService
import android.content.Intent

/**
 * IntentService 特点：
 * 1. 使用简单，自动开启线程处理耗时任务
 * 2. 处理完耗时任务后，自动（停止） Service
 *
 * 原理：
 * Handler + Thread 实现
 */
class MyIntentService: IntentService("MyIntentThread") {


    override fun onHandleIntent(intent: Intent?) {
        // 这个方法执行在子线程中，在这里可以直接处理耗时任务
    }
}