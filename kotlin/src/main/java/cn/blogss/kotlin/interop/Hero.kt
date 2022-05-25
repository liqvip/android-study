
// 3. 使用 @file:JvmName 注解指定编译类的名字。此文件默认是 HeroKt
@file:JvmName("Hero")

package cn.blogss.kotlin.interop

import java.io.IOException
import kotlin.jvm.Throws

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

    adversary.offerFood()

    // Kotlin 中的异常都是未检查异常
    try {
        adversary.extendHandInFriendship()
    } catch (e: Exception){
        println("Begone, foul beast!")
    }
}

fun makeProclamation() = "Greetings, beast!"


//4. 使用 JvmOverloads 注解生成 Kotlin 函数重载版本，方便 Java 调用
@JvmOverloads
fun handOverFood(leftHand: String = "berries", rightHand: String = "beef") {
    println("Mmm... you hand over some delicious $leftHand and $rightHand")
}


//8. Throws 注解告诉 Java 编译器这个方法会抛出异常
@Throws(IOException::class)
fun acceptApology(){
    throw IOException()
}

class Spellbook {
    //5. 给 Kotlin 属性使用 JvmField 注解，暴露给 Java 调用者，从而避免使用 getter 方法
    @JvmField
    val spells = listOf<String>("Magic Ms.L", "Lay on Hans")

    companion object {
        //6. JvmField 注解可以让在 Java 里直接访问伴生对象的成员
        @JvmField
        val MAX_SPELL_COUNT = 10

        //7. JvmStatic 注解类似于 JvmField，允许在 Java 中直接调用伴生对象里的函数
        @JvmStatic
        fun getSpellbookGreeting() = println("I am the Great Grimoire!")
    }
}



