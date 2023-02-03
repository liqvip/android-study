package cn.blogss.frame.okhttp;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * OkHttp official recipes
 * https://square.github.io/okhttp/recipes/
 * https://blog.csdn.net/mynameishuangshuai/article/details/51303446
 */
public class OkHttpRecipeTest {
    private final OkHttpClient okHttpClient = new OkHttpClient();
    /**
     * 1. 同步 Get 请求
     */

    public void synchronousGet() throws IOException {
        Request request = new Request.Builder()
                .url("https://publicobject.com/helloworld.txt")
                .build();

        try(Response response = okHttpClient.newCall(request).execute()) {
            if(!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            Headers responseHeaders = response.headers();
            for (int i=0;i <responseHeaders.size(); i++) {
                System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
            }
            /**
             * Server: nginx/1.10.3 (Ubuntu)
             * Date: Fri, 03 Feb 2023 09:18:44 GMT
             * Content-Type: text/plain
             * Content-Length: 1759
             * Last-Modified: Tue, 27 May 2014 02:35:47 GMT
             * Connection: keep-alive
             * ETag: "5383fa03-6df"
             * Accept-Ranges: bytes
             */

            // 如果 响应体 > 1MB，应避免使用 string() 方法，因为它会将整个文档加载到内存中。这种情况下，应该使用流的方式来处理 body
            System.out.println(response.body().string());
        }
    }

    /**
     * 2. 异步 Get 请求
     */
    public void asynchronousGet(){
        Request request = new Request.Builder()
                .url("https://publicobject.com/helloworld.txt")
                .build();

        // 在工作线程上下载文件，并在响应可读时被回调
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("Current thread: " + Thread.currentThread().getName());

                try(ResponseBody responseBody = response.body()) {
                    if(!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }

                    Headers responseHeaders = response.headers();
                    for (int i=0;i <responseHeaders.size(); i++) {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }

                    System.out.println(responseBody.string());
                }

            }
        });

    }


    /**
     * 3. 访问头字段
     */

    /**
     * 4. Post 方式提交 String
     */

    /**
     * 5. Post 方式提交流
     */

    /**
     * 6. Post 方式提交文件
     */

    /**
     * 7. Post 方式提交表单
     */

    /**
     * 8. Post方式提交分块请求
     */

    /**
     * 9. 使用 moshi 解析 JSON 响应
     */

    /**
     * 10. 缓存响应
     */

    /**
     * 11. 取消正在进行的调用
     */

    /**
     * 12. 设置超时
     */

    /**
     * 13. 改变单个 call 的配置
     */

    /**
     * 14. 处理身份验证
     */

}
