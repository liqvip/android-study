package cn.blogss.kotlin

/**
 * 扩展函数
 */

// 1. 一个普通的扩展函数
fun String.addEnthusiasm(amount: Int = 1) = this + "!".repeat(amount)

//2. 扩大适用范围，在超类上定义扩展函数
fun Any.easyPrint() = println(this)

//3. 定义泛型扩展函数
fun <T> T.easyPrintLink(): T {
    println(this)
    return this
}

//4. 可空类扩展
infix fun String?.printWithDefault(default: String) = println(this ?: default)

fun frame(name: String, padding: Int, formatChar: String = "*"): String{
    val greeting = "$name!"
    val middle = formatChar.padEnd(padding)
        .plus(greeting)
        .plus(formatChar.padStart(padding))
    val end = (middle.indices).joinToString("") { formatChar }
    return "$end\n$middle\n$end"
}

fun String.frame(padding: Int): String {
    val formatChar = "*"
    val greeting = "$this!"
    val middle = formatChar.padEnd(padding)
        .plus(greeting)
        .plus(formatChar.padStart(padding))
    val end = (middle.indices).joinToString("") { formatChar }
    return "$end\n$middle\n$end"
}


/**
 * 扩展属性
 */
// 1. 一个普通的扩展属性，一个有效的扩展属性必须自己定义 get 或 set 函数，算出它应返回的属性值
val String.numVowels
    get() = count { "aeiouy".contains(it) }