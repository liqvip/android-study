package cn.blogss.kotlin


/**
 * apply 扩展函数的返回值是调用者对象
 * apply 扩展函数只有一个参数，参数类型是带有接收者的函数类型
 * 这样在 lambda 表达式中就可以访问接收者对象的成员变量
 *
 * 使用场景：常用于初始化对象属性
 */

fun main() {
    val dog = Dog(age = 5).apply {
        // lambda 中可以直接访问接收者对象的成员
        name = "小黑"
    }
    println(dog.des)
}
