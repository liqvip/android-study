package cn.blogss.frame.okhttp;

import androidx.annotation.Nullable;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.Route;
import okio.BufferedSink;

/**
 * OkHttp official recipes
 * https://square.github.io/okhttp/recipes/
 * https://blog.csdn.net/mynameishuangshuai/article/details/51303446
 */
public class OkHttpRecipeTest {
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
    private static final String IMGUR_CLIENT_ID = "...";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    // Moshi
    private final Moshi moshi = new Moshi.Builder().build();
    private final JsonAdapter<Gist> gistJsonAdapter = moshi.adapter(Gist.class);

    // Cache, 配置 OkHttp 本地缓存
    int cacheSize = 10 * 1024 * 1024; // 10 MiB
    Cache cache = new Cache(new File("okhttpcache"), cacheSize);

    // client
    private final OkHttpClient okHttpClient = new OkHttpClient.Builder()
//            .cache(cache)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .callTimeout(10, TimeUnit.SECONDS)
            .build();

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

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
     * 3. 为一个头字段设置多个值
     */
    public void accessHeaders() throws Exception {
        Request request = new Request.Builder()
                .url("https://api.github.com/repos/square/okhttp/issues")
                .header("User-Agent", "OkHttp Headers.java")
                // OkHttp 支持一个头字段设置多个值
                .addHeader("Accept", "application/json; q=0.5")
                .addHeader("Accept", "application/vnd.github.v3+json")
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            /**
             * Server: GitHub.com
             * Date: Mon, 06 Feb 2023 07:16:13 GMT
             * Vary: [Accept, Accept-Encoding, Accept, X-Requested-With]
             */
            System.out.println("Server: " + response.header("Server"));
            System.out.println("Date: " + response.header("Date"));
            System.out.println("Vary: " + response.headers("Vary"));
        }
    }

    /**
     * 4. Post 方式提交 String
     */
    public void postString() throws Exception {
        String postBody = ""
                + "Releases\n"
                + "--------\n"
                + "\n"
                + " * _1.0_ May 6, 2013\n"
                + " * _1.1_ June 15, 2013\n"
                + " * _1.2_ August 11, 2013\n";

        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, postBody))
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            /**
             * <h2><a id="user-content-releases" class="anchor" aria-hidden="true" href="#releases"><span aria-hidden="true" class="octicon octicon-link"></span></a>Releases</h2>
             * <ul>
             * <li>
             * <em>1.0</em> May 6, 2013</li>
             * <li>
             * <em>1.1</em> June 15, 2013</li>
             * <li>
             * <em>1.2</em> August 11, 2013</li>
             * </ul>
             */
            System.out.println(response.body().string());
        }
    }

    /**
     * 5. Post 方式提交流
     */
    public void postStreaming() throws Exception {
        // 自己重写 contentType 和 writeTo 方法
        RequestBody requestBody = new RequestBody() {
            @Override public MediaType contentType() {
                return MEDIA_TYPE_MARKDOWN;
            }

            @Override public void writeTo(BufferedSink sink) throws IOException {
                sink.writeUtf8("Numbers\n");
                sink.writeUtf8("-------\n");
                for (int i = 2; i <= 997; i++) {
                    sink.writeUtf8(String.format(" * %s = %s\n", i, factor(i)));
                }
            }

            private String factor(int n) {
                for (int i = 2; i < n; i++) {
                    int x = n / i;
                    if (x * i == n) return factor(x) + " × " + i;
                }
                return Integer.toString(n);
            }
        };

        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(requestBody)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            /**
             * <h2><a id="user-content-numbers" class="anchor" aria-hidden="true" href="#numbers"><span aria-hidden="true" class="octicon octicon-link"></span></a>Numbers</h2>
             * <ul>
             * <li>2 = 2</li>
             * <li>3 = 3</li>
             * <li>4 = 2 × 2</li>
             * <li>5 = 5</li>
             * <li>6 = 3 × 2</li>
             * <li>7 = 7</li>
             * <li>8 = 2 × 2 × 2</li>
             * ...
             * <li>992 = 31 × 2 × 2 × 2 × 2 × 2</li>
             * <li>993 = 331 × 3</li>
             * <li>994 = 71 × 7 × 2</li>
             * <li>995 = 199 × 5</li>
             * <li>996 = 83 × 3 × 2 × 2</li>
             * <li>997 = 997</li>
             * </ul>
             */
            System.out.println(response.body().string());
        }
    }

    /**
     * 6. Post 方式提交文件
     */
    public void postFile() throws Exception {
        File file = new File("test.txt");

        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, file))
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            /**
             * <p>this is a test.</p>
             */
            System.out.println(response.body().string());
        }
    }

    /**
     * 7. Post 方式提交表单
     * FormBody 的使用
     */
    public void postForm() throws Exception {
        RequestBody formBody = new FormBody.Builder()
                .add("search", "Jurassic Park")
                .build();
        Request request = new Request.Builder()
                // 被墙
                // java.net.SocketTimeoutException: connect timed out
                .url("https://en.wikipedia.org/w/index.php")
                .post(formBody)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            System.out.println(response.body().string());
        }
    }

    /**
     * 8. Post方式提交分块请求
     * MultipartBody 的使用
     */
    public void postMultipartBody() throws Exception {
        // Use the imgur image upload API as documented at https://api.imgur.com/endpoints/image
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", "Square Logo")
                .addFormDataPart("image", "logo-square.png",
                        RequestBody.create(MEDIA_TYPE_PNG, new File("logo-square.png")))
                .build();

        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                // 被墙
                // java.net.SocketTimeoutException: connect timed out
                .url("https://api.imgur.com/3/image")
                .post(requestBody)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            System.out.println(response.body().string());
        }
    }

    /**
     * 9. 使用 moshi 解析 JSON 响应
     */
    public void moshiParse() throws Exception {
        Request request = new Request.Builder()
                .url("https://api.github.com/gists/c2a7c39532239ff261be")
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            Gist gist = gistJsonAdapter.fromJson(response.body().source());

            for (Map.Entry<String, GistFile> entry : gist.files.entrySet()) {
                System.out.println(entry.getKey());
                System.out.println(entry.getValue().content);
            }
        }
    }

    static class Gist {
        Map<String, GistFile> files;
    }

    static class GistFile {
        String content;
    }

    /**
     * 10. 缓存响应
     */
    public void cacheResponse() throws Exception {
        Request request = new Request.Builder()
                .url("http://publicobject.com/helloworld.txt")
                .build();

        String response1Body;
        try (Response response1 = okHttpClient.newCall(request).execute()) {
            if (!response1.isSuccessful()) throw new IOException("Unexpected code " + response1);

            /**
             * 第一次调用打印
             * Response 1 response:          Response{protocol=http/1.1, code=200, message=OK, url=https://publicobject.com/helloworld.txt}
             * Response 1 cache response:    null
             * Response 1 network response:  Response{protocol=http/1.1, code=200, message=OK, url=https://publicobject.com/helloworld.txt}
             *
             * Response 2 response:          Response{protocol=http/1.1, code=200, message=OK, url=https://publicobject.com/helloworld.txt}
             * Response 2 cache response:    Response{protocol=http/1.1, code=200, message=OK, url=https://publicobject.com/helloworld.txt}
             * Response 2 network response:  null
             *
             * Response 2 equals Response 1? true
             */

            /**
             * 第二次调用打印
             * Response 1 response:          Response{protocol=http/1.1, code=200, message=OK, url=https://publicobject.com/helloworld.txt}
             * Response 1 cache response:    Response{protocol=http/1.1, code=200, message=OK, url=https://publicobject.com/helloworld.txt}
             * Response 1 network response:  null
             *
             * Response 2 response:          Response{protocol=http/1.1, code=200, message=OK, url=https://publicobject.com/helloworld.txt}
             * Response 2 cache response:    Response{protocol=http/1.1, code=200, message=OK, url=https://publicobject.com/helloworld.txt}
             * Response 2 network response:  null
             *
             * Response 2 equals Response 1? true
             */
            response1Body = response1.body().string();
            System.out.println("Response 1 response:          " + response1);
            System.out.println("Response 1 cache response:    " + response1.cacheResponse());
            System.out.println("Response 1 network response:  " + response1.networkResponse());
        }

        String response2Body;
        try (Response response2 = okHttpClient.newCall(request).execute()) {
            if (!response2.isSuccessful()) throw new IOException("Unexpected code " + response2);
            response2Body = response2.body().string();
            System.out.println("Response 2 response:          " + response2);
            System.out.println("Response 2 cache response:    " + response2.cacheResponse());
            System.out.println("Response 2 network response:  " + response2.networkResponse());
        }

        System.out.println("Response 2 equals Response 1? " + response1Body.equals(response2Body));
    }

    /**
     * 11. 取消正在进行的请求
     * 使用 call.cancel() 立即停止正在进行的调用。如果线程当前正在写入请求或读取响应，它将收到一个 IOException。
     * 当不再需要调用时，使用此功能可以节省网络。例如，当用户导航离开应用程序时，同步和异步调用都可以取消。
     */
    public void cancelCall() throws Exception {
        Request request = new Request.Builder()
                .url("http://httpbin.org/delay/2") // This URL is served with a 2 second delay.
                .build();

        final long startNanos = System.nanoTime();
        final Call call = okHttpClient.newCall(request);

        /**
         * 0.00 Executing call.
         * 1.02 Canceling call.
         * 1.02 Canceled call.
         * 1.02 Call failed as expected: java.io.IOException: Canceled
         */

        // Schedule a job to cancel the call in 1 second.
        executor.schedule(new Runnable() {
            @Override public void run() {
                System.out.printf("%.2f Canceling call.%n", (System.nanoTime() - startNanos) / 1e9f);
                call.cancel();
                System.out.printf("%.2f Canceled call.%n", (System.nanoTime() - startNanos) / 1e9f);
            }
        }, 1, TimeUnit.SECONDS);

        System.out.printf("%.2f Executing call.%n", (System.nanoTime() - startNanos) / 1e9f);
        try (Response response = call.execute()) {
            System.out.printf("%.2f Call was expected to fail, but completed: %s%n",
                    (System.nanoTime() - startNanos) / 1e9f, response);
        } catch (IOException e) {
            System.out.printf("%.2f Call failed as expected: %s%n",
                    (System.nanoTime() - startNanos) / 1e9f, e);
        }
    }

    /**
     * 12. 设置超时
     * OkHttp支持连接、写、读和完全调用超时。
     */
    public void timeout() throws Exception {
        /**
         * callTimeout(10, TimeUnit.SECONDS) 情况下
         *
         * java.io.InterruptedIOException: timeout
         * 	at okhttp3.internal.connection.RealCall.timeoutExit(RealCall.kt:398)
         * 	at okhttp3.internal.connection.RealCall.callDone(RealCall.kt:360)
         */
        Request request = new Request.Builder()
                .url("http://httpbin.org/delay/10") // This URL is served with a 10 second delay.
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            System.out.println("Response completed: " + response);
        }
    }

    /**
     * 13. 改变单个 call 的配置
     * 原理：浅拷贝 OkHttpClient
     */
    public void perCallConfiguration() throws Exception {
        Request request = new Request.Builder()
                .url("http://httpbin.org/delay/1") // This URL is served with a 1 second delay.
                .build();

        /**
         * Response 1 failed: java.net.SocketTimeoutException: Read timed out
         * Response 2 succeeded: Response{protocol=http/1.1, code=200, message=OK, url=http://httpbin.org/delay/1}
         */

        // Copy to customize OkHttp for this request.
        OkHttpClient client1 = okHttpClient.newBuilder()
                .readTimeout(500, TimeUnit.MILLISECONDS)
                .build();
        try (Response response = client1.newCall(request).execute()) {
            System.out.println("Response 1 succeeded: " + response);
        } catch (IOException e) {
            System.out.println("Response 1 failed: " + e);
        }

        // Copy to customize OkHttp for this request.
        OkHttpClient client2 = okHttpClient.newBuilder()
                .readTimeout(3000, TimeUnit.MILLISECONDS)
                .build();
        try (Response response = client2.newCall(request).execute()) {
            System.out.println("Response 2 succeeded: " + response);
        } catch (IOException e) {
            System.out.println("Response 2 failed: " + e);
        }
    }

    /**
     * 14. 处理身份验证
     * OkHttp 可以自动重试未经身份验证的请求。当响应为 401 未授权时，要求验证者提供凭据。实现应该构建一个包含缺失凭据的新请求。
     * 如果没有可用的凭据，则返回 null 以跳过重试。
     * 使用 Response.challenges() 获取服务器告知的身份验证方案和受保护的资源范围。当实现一个 Basic 认证时，使用 Credentials.Basic(用户名、密码) 对请求头进行编码。
     */
    public void authenticate() throws Exception {
        OkHttpClient okHttpClient1 = okHttpClient.newBuilder()
                .authenticator(new Authenticator() {
                    @Nullable
                    @Override
                    public Request authenticate(@Nullable Route route, Response response) throws IOException {
                        if (response.request().header("Authorization") != null) {
                            return null; // Give up, we've already attempted to authenticate.
                        }

                        /**
                         * Authenticating for response: Response{protocol=http/1.1, code=401, message=Unauthorized, url=https://publicobject.com/secrets/hellosecret.txt}
                         * Challenges: [Basic authParams={realm=OkHttp Secrets}]
                         */
                        System.out.println("Authenticating for response: " + response);
                        System.out.println("Challenges: " + response.challenges());
                        String credential = Credentials.basic("jesse", "password1");
                        return response.request().newBuilder()
                                .header("Authorization", credential)
                                .build();
                    }
                })
                .build();

        Request request = new Request.Builder()
                .url("http://publicobject.com/secrets/hellosecret.txt")
                .build();

        try (Response response = okHttpClient1.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            System.out.println(response.body().string());
        }
    }

}
