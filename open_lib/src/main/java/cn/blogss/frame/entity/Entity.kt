package cn.blogss.frame.entity

import kotlinx.serialization.Serializable

@Serializable
data class GirlImageResponse(
    val code: Int,
    val msg: String,
    val url: String
)

enum class Server(val server: String){
    VMY("vmy"),
    FREE_API("freeApi")
}

enum class BaseUrl(val url: String) {
    // https://api.52vmy.cn/
    VMY("https://api.52vmy.cn"),
    // https://www.free-api.com/
    FREE_API("https://cn.apihz.cn")
}

enum class VMYApi(val api: String) {
    GIRL("/api/img/tu/girl"),
    BOY("/api/img/tu/boy"),
}