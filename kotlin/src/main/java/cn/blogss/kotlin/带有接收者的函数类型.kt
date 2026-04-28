package cn.blogss.kotlin

data class Person(val name: String, var age: Int)


/**
 * 下面是 add 函数反编译成 java 后的的代码
 * add 实际变成了 add$lambda$0(Person var0) 的形式
 * 将接收者对象作为参数传入了 add$lambda$0 方法
 *
 *    private static final Unit add$lambda$0(Person var0) {
 *       Intrinsics.checkNotNullParameter(var0, "<this>");
 *       var0.setAge(var0.getAge() + 1);
 *       return Unit.INSTANCE;
 *    }
 */
val add: Person.() -> Unit = {
    age += 1
}

fun main() {
    val person = Person("John", 25)
    // 输出: Name: John, Age: 25
    println("Name: ${person.name}, Age: ${person.age}")
    // 在 Lambda 表达式中，可以直接访问 Person 类的成员
    person.add()
}