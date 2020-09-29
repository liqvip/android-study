package cn.blogss.helper.originhttprequest

/**
 * @创建人 560266
 * @文件描述 网络请求回调接口
 * @创建时间 2020/9/17
 */
interface OnRequestListener {
    fun onOK(result: String)
    fun onFail()
}