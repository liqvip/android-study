package cn.blogss.core.network.http;

/**
 * @Descripttion: 网络请求回调接口
 */
public interface OnListener {
    void onFail(Throwable e);

    void onOk(String s);
}
