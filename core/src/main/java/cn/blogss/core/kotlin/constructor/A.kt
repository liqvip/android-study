package cn.blogss.core.kotlin.constructor

/**
 * 构造函数
 */
class A (var name: String){
                                        //直接调用主构造器
    constructor(name: String, id: Int) : this(name) {}
                                                    //通过上一个次构造器，间接调用主构造器
    constructor(name: String, id: Int, age: Int) : this(name, id){

    }
}