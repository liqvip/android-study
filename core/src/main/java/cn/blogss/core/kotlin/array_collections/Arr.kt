package cn.blogss.core.kotlin.array_collections

/**
 * @创建人 560266
 * @文件描述
 * @创建时间 2020/7/28
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