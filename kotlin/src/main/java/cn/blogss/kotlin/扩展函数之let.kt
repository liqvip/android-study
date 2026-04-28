package cn.blogss.kotlin

/**
 * let 函数的返回值是 lambda 表达式的返回值
 * 可以在 lambda 中使用 it 来访问自身
 *
 * 使用场景:处理非空对象，对象映射转换
 */

fun main() {
    val dog: Dog? = Dog()
    dog?.let {
        println(it.des)
    }
}