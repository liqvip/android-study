package cn.blogss.kotlin

class Rectangle(val width: Int, val height: Int) {
    var area: Int = 0
        get() {
            return this.width * this.height
        }
        set(value){
            field = value * this.height
        }
}

fun main() {
    val rectangle = Rectangle(3, 4)
    println("${rectangle.area}")
    rectangle.area = 5
    println("${rectangle.area}")
    println()
}