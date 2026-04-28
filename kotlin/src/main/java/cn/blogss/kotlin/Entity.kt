package cn.blogss.kotlin

data class Dog(var name: String = "小白", val age: Int = 2) {
    val des: String
        get() = "我的名字是$name, 我今年${age}岁啦"
}

class Rectangle(val width: Int, val height: Int) {
    var area: Int = 0
        get() {
            return this.width * this.height
        }
        set(value){
            field = value * this.height
        }
}
