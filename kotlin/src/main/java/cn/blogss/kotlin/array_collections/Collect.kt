package cn.blogss.kotlin.array_collections

/**
 * kotlin 集合
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