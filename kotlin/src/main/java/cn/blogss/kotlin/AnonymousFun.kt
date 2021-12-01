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
    val greetingFunction1: () -> String = {
        val currentYear = 2021
        "Welcome to SimVillage, Mayor! (Copyright $currentYear)"
    }


    // 匿名函数的参数
    val greetingFunction2: (String) -> String  = { player ->
        val currentYear = 2021
        "Welcome to SimVillage, $player! (Copyright $currentYear)"
    }

    // it 关键字，定义只有一个参数的匿名函数时，可以使用 it 关键字来表示参数名
    val greetingFunction3: (String) -> String  = {
        val currentYear = 2021
        "Welcome to SimVillage, $it! (Copyright $currentYear)"
    }

    // println(greetingFunction3("Guyal"))


    // 类型推断，不带参数的匿名函数，可以省略函数类型
    val greetingFunction4 = {
        val currentYear = 2021
        "Welcome to SimVillage, Mayor! (Copyright $currentYear)"
    }

    // 类型推断，带参数的匿名函数，可以省略函数类型
    val greetingFunction5 = { playerName: String, numBuildings: Int ->
        val currentYear = 2021
        println("Adding $numBuildings houses")
        "Welcome to SimVillage, $playerName! (Copyright $currentYear)"
    }

    // println(greetingFunction5("Guyal",2))


    // 定义参数是函数的函数
    fun runSimulation(playerName: String, greetingFunction: (String, Int) -> String){
        val numBuildings = (1..3).shuffled().last()
        println(greetingFunction(playerName, numBuildings))
    }

    // runSimulation("Guyal",greetingFunction5)

    // 简略语法，如果一个函数的 lambda 参数排在最后，或者是唯一的参数，那么括住 lambda 值参的一对圆括号就可以省略
    /*runSimulation("Guyal") { playerName, numBuildings ->
        val currentYear = 2021
        println("Adding $numBuildings houses")
        "Welcome to SimVillage, $playerName! (Copyright $currentYear)"
    }*/


    // 函数内联
    inline fun runSimulation2(playerName: String, greetingFunction: (String, Int) -> String){
        val numBuildings = (1..3).shuffled().last()
        println(greetingFunction(playerName, numBuildings))
    }

    // 函数引用，除了传递 lambda 表达式，还可以传递函数引用
    fun printConstructionSet(numBuildings: Int) {
        val cost = 500
        println("construction cost: ${cost * numBuildings}")
    }

    inline fun runSimulation3(playerName: String, costPrinter: (Int) -> Unit, greetingFunction: (String, Int) -> String){
        val numBuildings  = (1..3).shuffled().last()
        costPrinter(numBuildings)
        println(greetingFunction(playerName, numBuildings))
    }

    /*runSimulation3("Guyal",::printConstructionSet) { playerName, numBuildings ->
        val currentYear = 2021
        println("Adding $numBuildings houses")
        "Welcome to SimVillage, $playerName! (Copyright $currentYear)"
    }*/


    // 函数类型作为返回类型
    private fun configureGreetingFunction(): (String) -> String {
        val structureType = "hospitals"
        var numBuildings = 5
        return { playerName: String ->
            val currentYear = 2021
            numBuildings += 1
            println("Adding $numBuildings houses")
            "Welcome to SimVillage, $playerName! (Copyright $currentYear)"
        }
    }

    fun runSimulation4() {
        val greetingFunction = configureGreetingFunction()
        println(greetingFunction("Guyal"))
    }

    // runSimulation4()



}