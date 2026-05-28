package cn.blogss.kotlin.basic

/**
 * [Conditions and loops](https://kotlinlang.org/docs/control-flow.html#for-loops)
 * 使用 for 循环来遍历一个集合、数组或区间
 */
fun main() {
    println("1.遍历闭区间")
    for (i in 1.rangeTo(6)) {
        println(i)
    }

    println("2.遍历开区间")
    for (i in 1.rangeUntil(6)) {
        println(i)
    }

    println("3.遍历数组")
    val arr = arrayOf(1, 2, 3)
    for (i in arr.indices) {
        println(arr[i])
    }

    /**
     * 遍历集合类型，使用 for 也可以
     * 但是如果需要删除元素，应该使用迭代器
     */

    println("4. 遍历列表，使用 for")
    val list = mutableListOf("1", "2", "3")
    val iterator = list.iterator()

    for(i in list) {
        println(i)
    }

    println("5. 遍历列表，使用迭代器")
    while (iterator.hasNext()) {
        val i = iterator.next()
        if(i == "1")  iterator.remove()
        println(iterator.next())
    }
}