package cn.blogss.kotlin.constant

/**
 * @创建人 560266
 * @文件描述
 * @创建时间 2020/7/27
 */
class A {
    var number: Int = 1
    companion object {
        var age: Int = 18
        fun method(){
            print("method()")
        }
    }

    object B{
        var name:String = "B"
        fun method(){
            print("method()")
        }
    }
}