package cn.blogss.kotlin



fun main() {
    val rectangle = Rectangle(3, 4)
    println("${rectangle.area}")
    rectangle.area = 5
    println("${rectangle.area}")
    println()
}