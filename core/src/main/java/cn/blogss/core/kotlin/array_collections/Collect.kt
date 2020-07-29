package cn.blogss.core.kotlin.array_collections

/**
 * @创建人 560266
 * @文件描述
 * @创建时间 2020/7/28
 */
class Collect {
    //List
    var strList = listOf("a", "b", "c")

    var mtStrList = strList.toMutableList()

    var strSet = setOf("a", "b", "c")

    var mtStrSet = mtStrList.toMutableSet()

    var map = mapOf("key1" to 1, "key2" to 2, "key3" to 3)

    var mtMap = map.toMutableMap()

    var seq = sequenceOf("a", "b", "c")

    var sList = listOf("a", "b", "c")
    var seqList = sList.asSequence()

    var lambdaSeq = generateSequence(0) { it + 1 }


    fun print(){
        val value1 = map.get("key1")
        val value2 = map["key2"]

        mtMap.put("key1", 2)
        mtMap["key1"] = 2


    }
}