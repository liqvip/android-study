
// 3. 使用 @file:JvmName 注解指定编译类的名字。此文件默认是 HeroKt
@file:JvmName("Hero")

package cn.blogss.kotlin.interop

/**
 * @author: Little Bei
 * @Date: 2022/5/23
 */

fun main(args: Array<String>){
    val adversary = Jhava()
    println(adversary.utterGreeting())

    // 1. 使用可空性注解
    val friendshipLevel = adversary.determineFriendshipLevel()
    println(friendshipLevel?.toLowerCase())
    println(friendshipLevel?.toLowerCase() ?: "It's complicated.")

    // 2. Kotlin 与 Java 类型映射
    val adversaryHitPoint = adversary.hitPoints
    println(adversaryHitPoint.dec())
    println(adversaryHitPoint.javaClass)


}

fun makeProclamation() = "Greetings, beast!"


//4. 使用 JvmOverloads 注解生成 Kotlin 函数重载版本，方便 Java 调用
@JvmOverloads
fun handOverFood(leftHand: String = "berries", rightHand: String = "beef") {
    println("Mmm... you hand over some delicious $leftHand and $rightHand")
}