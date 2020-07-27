package cn.blogss.helper.notificationutil

/**
 * @创建人 560266
 * @文件描述 通知渠道名称
 * @创建时间 2020/7/27
 */
enum class ChannelName(val index: Int, val channelName: String) {
    DEFAULT(0, "系统通知"),

    MESSAGE(1, "聊天消息"),

    AD(2, "广告推送"),
}