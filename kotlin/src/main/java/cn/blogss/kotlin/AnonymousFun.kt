package cn.blogss.kotlin


/**
 * 匿名函数
 */
class AnonymousFun {

    private val numLettersFunc: (Char) -> Boolean = { letter ->
        letter == 's'
    }

    val numLetters1 = "Mississippi".count(numLettersFunc)

    val numLetters2 = "Mississippi".count { letter ->
        letter == 's'
    }

    val numLetters3 = "Mississippi".count{
        it == 's'
    }

    // 调用匿名函数
    fun simVillage(){
        println({
            val currentYear = 2021
            "Welcome to SimVillage, Mayor! (Copyright $currentYear)"
        }())
    }

    // 匿名函数的类型 () -> String，隐式返回
    val greetingFunc: () -> String = {
        val currentYear = 2021
        "Welcome to SimVillage, Mayor! (Copyright $currentYear)"
    }

    // 匿名函数的参数
    val greetingFunction: (String) -> String  = { player ->
        val currentYear = 2021
        "Welcome to SimVillage, $player! (Copyright $currentYear)"
    }

    // it 关键字，定义只有一个参数的匿名函数时，可以使用 it 关键字来表示参数名


    // 定义参数是函数的函数

}