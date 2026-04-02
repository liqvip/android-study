package cn.blogss.kotlin

data class Person(var name: String, var age: Int)

fun main() {
    var person: Person? = Person("小明", 18)
    val person2 = person
    person = null

    println("$person")
    println(person2.toString())
}
