package cn.blogss.kotlin

import android.view.View

/**
 * Kotlin 基础
 */
class Base {
    /*
    * kotlin 的空安全设计：
    * 属性需要在声明的时候同时初始化(不能为null,否则报错)，因为kotlin 的变量是没有默认值的，可用下面几种方法：
    * 1. 声明成抽象的(kotlin特有的功能)
    * 2. 使用 lateinit 告诉编译器我没法第一时间初始化，但我使用之前肯定会初始化
    * 3. 使用 ?, 允许为空
    */
    lateinit var view1: View

    var view2: View? = null

    var name: String? = null

    /* kotlin 类型推断：
     * 变量在声明的时候就赋值，可以不写变量类型
     * 注意和动态类型的区别
     * */
    var age = 18

    var sex = "男"

    var height = 1.8

    /*
    * 只读变量：
    * 1. 只能赋值一次，不能修改
    * 2. 注意和 java 关键字 final 有点不一样
    * */
    val id: String = "1"
}

class Base2 {
    var base: Base = Base()

    /*
    * kotlin 函数声明方式：
    * 1. 函数无返回值，可以使用 Unit。也可以省略
    * 2. kotlin 有默认的 getter/setter 支持
    * */
    fun print(): Unit{
        /*使用一个可能为空的变量，可以使用 ? 如果为空则不调用。也可以使用 !! 这个会直接报错*/
        print(base.name?.length)
        /*实际上调用了print(base.getAge())*/
        print(base.age)
        /*实际上调用了base.setAge(28)*/
        base.age = 28
    }

    /*函数参数也有可空的控制*/
    fun cook(name: String?): Base {
        return Base()
    }
}

open class Base3 {
    /*
    * kotlin 为变量设置钩子代码：
    * 1. 因为kotlin 有默认的 getter/setter 支持，所以可以为每个变量设置钩子代码
    * */
    var food: String? = null
    get() {
        return if(field == null){
            ""
        }else{
            field
        }
    }
    set(value) {
        field = value ?: ""
    }

    /*对于 val 声明的变量是不能重写 setter 函数的，但是可以重写 getter 函数*/
    val water: String = "sweet"
    get() {
        if(field == "sweet")
            return field
        return "no"
    }
}

/*kotlin 里的类默认是 final 的，使用 open 可以解除 final 限制*/
open class Base4: Base3() {
    /*
    * kotlin 基本类型
    * 1. 在 kotlin 中，所有东西都是对象。
    * */
    var number : Int = 1    //还有 Double Float Long Short Byte

    var c : Char = 'c'

    var b : Boolean = true

    var array: IntArray = intArrayOf(1, 2)  //还有 FloatArray DoubleArray CharArray 等

    var str: String = "string"

    fun action(){}

    fun main(){
        var base: Base3 = Base4()
        /*使用 is 关键字进行[类型判断]，因为编译器能够进行类型推断，可以帮助我们省略强转的写法*/
        if(base is Base4)
            base.action()
        /*使用 as 直接进行强转调用，不进行类型判断。但如果强转成一个错误的类型，程序就会抛出一个异常*/
        (base as Base4).action()
        /*使用 as? ，强转成功就执行之后的调用，否则就不执行。推荐使用这种方法*/
        (base as? Base4)?.action()
    }
}

interface impl{
    fun haveError(): Boolean
    fun haveMsg(): Boolean
}

/*kotlin 里的 override 函数可见性是继承自父类的*/
class Base5 : Base4, impl{
    constructor(name: String, age: String){
        /*注意会默认调用super();*/
    }
    override fun haveError(): Boolean {
        TODO("Not yet implemented")
    }

    override fun haveMsg(): Boolean {
        TODO("Not yet implemented")
    }
}

