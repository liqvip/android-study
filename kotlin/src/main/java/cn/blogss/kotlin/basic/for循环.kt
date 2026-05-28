package cn.blogss.kotlin.basic

/**
 * [Conditions and loops](https://kotlinlang.org/docs/control-flow.html#for-loops)
 * 使用 for 循环来遍历一个集合、数组或区间
 */
fun main() {
    println("遍历闭区间")
    for (i in 1.rangeTo(6)) {
        println(i)
    }
}