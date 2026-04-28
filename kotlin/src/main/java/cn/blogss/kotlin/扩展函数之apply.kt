package cn.blogss.kotlin


/**
 * apply 扩展函数的返回值是调用者对象
 * apply 扩展函数只有一个参数，参数类型是带有接收者的函数类型
 * 这样在 lambda 表达式中就可以访问接收者对象的成员变量
 *
 * 使用场景：在需要配置对象属性的时候
 *
 */
data class Dog(var name: String = "小白", val age: Int = 2) {
    val des: String
        get() = "我的名字是$name, 我今年${age}岁啦"
}

fun main() {
    val dog = Dog(age = 5).apply {
        // lambda 中可以直接访问接收者对象的成员
        name = "小黑"
    }
    println(dog.des)
}
