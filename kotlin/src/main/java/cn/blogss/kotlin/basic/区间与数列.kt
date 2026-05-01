package cn.blogss.kotlin.basic

/**
 * [区间与数列](https://kotlinlang.org/docs/ranges.html)
 *
 * Kotlin 能让你用 rangeTo() 和 rangeUntil() 这两个函数，轻松创建数值范围。
 * rangeTo 函数使用 .. 操作符，返回 IntRange 对象，闭区间
 * rangeUntil 函数使用 ..< 操作符，返回 IntRange 对象，左闭右开区间
 */
fun main() {
    // rangeTo
    val range = 1 .. 5
    for(i in range) {
        println(i)
    }
    println("\n")

    // rangeUntil
    val range2 = 1 ..< 5
    for (i in range2) {
        println(i)
    }

    println("\n")

    // 数列，自定义步长
    val range3 = 1 .. 10 step 2
    for (i in range3) {
        println(i)
    }

    println("\n")

    // 数列，倒序遍历
    val range4 = 5 downTo 1
    for (i in range4) {
        println(i)
    }
}