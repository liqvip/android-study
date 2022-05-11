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
    fun start(){
        runBlocking {
            launch(Dispatchers.Main) {
                val user = getUserInfo()
                println(user.age)
            }
        }

        runBlocking {
            val res = async {

            }
            val realRes = res.await()
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