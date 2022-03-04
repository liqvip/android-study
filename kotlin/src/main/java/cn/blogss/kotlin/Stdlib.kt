package cn.blogss.kotlin

import java.io.File

/**
 * Kotlin 标准库函数
 */
class Stdlib {

    // 1. apply 配置函数，相关作用域，接收者的隐式调用
    val menuFile = File("menu-file.txt").apply {
        setReadable(true)
        setWritable(true)
        setExecutable(true)
    }

    // 2. run，相关作用域，返回的是 lambbda 结果，可以执行函数引用
    fun nameIsLong(name: String) = name.length >= 20

    fun playerCreateMessage(nameTooLong:Boolean): String {
        return if(nameTooLong){
            "Name is to long. Please choose another name."
        }else{
            "Welcome, adventurer"
        }
    }

/*    "Polarcubis, Supreme Master of NyetHack"
        .run(::nameIsLong)
        .run(::playerCreateMessage)
        .run(::println)*/

    // 3. with，run 的变体

    // 4. let，使某个变量作用于其 lambda 表达式，返回 lambda 结果，?: 运算符
    fun formatGreeting(vipGuest: String?): String {
        return vipGuest?.let {
            "Welcome $it. Please, go straight back - your table is ready."
        } ?:"Welcome to the tavern. You'll be seated soon."
    }

    // 5. also，与 let 相似，不同的是 also 返回接收者对象

    // 6. takeif，返回接收者对象
    val fileContents = File("myfile.txt").takeIf {
        it.canRead() && it.canWrite()
    }?.readText()


}