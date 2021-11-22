package cn.blogss.kotlin.array_collections

/**
 * kotlin 数组
 */
class Arr {
    /*声明一个整形数组*/
    var ints: Array<Int> = arrayOf(1, 2, 3)

    /*声明一个字符串数组*/
    var strs: Array<String> = arrayOf("a", "b", "c")

    //var anys: Array<Any> = strs

    fun print(){
        println(strs[0])
        strs[1] = "A"
    }
}