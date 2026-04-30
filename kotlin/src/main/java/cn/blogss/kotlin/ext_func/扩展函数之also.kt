package cn.blogss.kotlin.ext_func

import cn.blogss.kotlin.Dog

/**
 * also 扩展函数的返回值是调用者对象
 * 特点是可以在 lambda 中使用 it 来访问自身
 *
 * 使用场景:常用于执行额外操作（如日志记录、数据校验），适用于侧重不改变对象主体结构的逻辑
 */
fun main() {

    val dog = Dog()
    dog.also {
        if(it.age < 5) println("你是一只年轻的狗狗")
    }
}