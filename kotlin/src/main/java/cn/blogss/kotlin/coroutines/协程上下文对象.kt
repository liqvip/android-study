package cn.blogss.kotlin.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.EmptyCoroutineContext

fun main() = runBlocking {
    val coroutineName = CoroutineName("my")
    launch(EmptyCoroutineContext + Dispatchers.Default + coroutineName) {
        println(coroutineContext.job)
        println(coroutineContext[ContinuationInterceptor])
        println(coroutineContext[CoroutineName]?.name)
    }
    println("end")
}