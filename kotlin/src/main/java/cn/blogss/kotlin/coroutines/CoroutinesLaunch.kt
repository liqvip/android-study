package cn.blogss.kotlin.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

/**
 * 协程启动
 */
class CoroutinesLaunch {
    val sum: Int.(Int) -> Int = { other ->
        plus(other)
    }

    fun add(){
        println(3.sum(33))
        val res2 = "22".let {
            "$it hello"
        }
        println(res2)
    }

    suspend fun method1(){
        CoroutineScope(EmptyCoroutineContext)
        val res = coroutineScope {
            launch {

            }
        }

    }
}