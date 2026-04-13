package cn.blogss.kotlin.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.EmptyCoroutineContext

fun main()  = runBlocking<Unit>{
    val scope = CoroutineScope(EmptyCoroutineContext)
    var innerScope: CoroutineScope? = null
    var innerJob: Job? = null
    scope.apply {

    }

    val job = scope.launch {
        innerScope = this
        innerJob = coroutineContext[Job]
    }

    println("job: $job")
    println("innerJob: $innerJob")
    println("innerScope: $innerScope")
    println("job === innerJob: ${job === innerJob}")
    println("job === innerScope: ${job === innerScope}")
}