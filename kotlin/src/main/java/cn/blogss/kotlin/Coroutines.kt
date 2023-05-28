package cn.blogss.kotlin

import kotlinx.coroutines.*

/**
 * 协程
 */
class Coroutines {
    /**
     * 三种启动方式
     * 1. launch
     * 2. runBlocking
     * 3. async
     */


    fun method1(){
        runBlocking(Dispatchers.IO) {
            println("currentThread: ${Thread.currentThread().name}")
            for(i in 0..5) {
                println("$i ")
            }
        }

        println("currentThread: ${Thread.currentThread().name} end")
    }

    fun method2(){
        runBlocking(Dispatchers.IO) {
            val job = launch {
                println("currentThread: ${Thread.currentThread().name}")
                for(i in 0..5) {
                    println("$i ")
                }
            }
            job.join()
            println("currentThread: ${Thread.currentThread().name} end")
        }
    }

    fun method3(){
        runBlocking(Dispatchers.IO) {
            val job = async {
                println("currentThread: ${Thread.currentThread().name}")
                for(i in 0..5) {
                    println("$i ")
                }
                10
            }
            println("return res: ${job.await()}")
            println("currentThread: ${Thread.currentThread().name} end")
        }
    }

    fun method4(){
        runBlocking {
            launch(Dispatchers.Main) {
                val user = getUserInfo()
                println(user.age)
            }
        }
    }

    private suspend fun getUserInfo(): User {
        return withContext(Dispatchers.IO) {
            delay(3000)
            User("liq", "18")
        }
    }

    data class User(var name: String ,var age: String)
}