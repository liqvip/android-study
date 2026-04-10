package cn.blogss.frame.entity

import kotlinx.serialization.Serializable

@Serializable
data class GirlImageResponse(
    val code: Int,
    val msg: String,
    val url: String
)