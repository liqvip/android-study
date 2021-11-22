package cn.blogss.network.http;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * http 网络请求
 */
public class Http{
    final static String TAG = Http.class.getSimpleName();

    private static OkHttpClient httpClient;
    private static MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    static {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.dns(new XDns(2000));
        httpClient = builder.build();
    }

    /**
     * rxjava 异步 post 请求，线程切换
     * @param url
     * @param token
     * @param params
     * @param listener
     */
    public static void post(final String url, final String token, final String params, final OnListener listener) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<String> emitter) {
                Log.i(TAG, "Post 请求接口：" + url +" 参数：" + params + " token：" + token);
                Request.Builder builder = new Request.Builder();
                if(!TextUtils.isEmpty(token)){
                    builder.addHeader("Authorization",token);
                }
                RequestBody requestBody = RequestBody.create(JSON,params);
                Request getReq = builder.url(url).post(requestBody).build();
                httpClient.newCall(getReq).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i(TAG, "post 请求失败: "+e.getMessage()+","+e.getCause());
                        emitter.onError(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String s = response.body().string();
                        Log.i(TAG, "post 请求成功：" + s);
                        emitter.onNext(s);
                        emitter.onComplete();
                    }
                });
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull String s) {
                listener.onOk(s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                if(e instanceof SocketTimeoutException){
                }else if(e instanceof ConnectException){
                }else if(e instanceof UnknownHostException){
                }
                listener.onFail(e);
            }

            @Override
            public void onComplete() {

            }
        });

    }


    /**
     * rxjava 异步 get 请求，线程切换
     * @param url
     * @param token
     * @param params
     * @param listener
     */
    public static void get(final StringBuilder url, final String token, final String params, final OnListener listener) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<String> emitter) {
                Log.i(TAG, "Get 请求接口：" + url +" 参数：" + params + " token：" + token);
                Request.Builder builder = new Request.Builder();
                if(!TextUtils.isEmpty(params)){
                    url.append("?");
                    url.append(params);
                }
                if(!TextUtils.isEmpty(token)){
                    builder.addHeader("Authorization",token);
                }
                Request getReq = builder.url(url.toString()).get().build();
                httpClient.newCall(getReq).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i(TAG, "get 请求失败: "+e.getMessage()+","+e.getCause());
                        emitter.onError(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String s = response.body().string();
                        Log.i(TAG, "Get 请求成功：" + s);
                        emitter.onNext(s);
                        emitter.onComplete();
                    }
                });
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull String s) {
                listener.onOk(s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                if(e instanceof SocketTimeoutException){
                }else if(e instanceof ConnectException){
                }else if(e instanceof UnknownHostException){
                }
                listener.onFail(e);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * rxjava 异步 delete 请求，线程切换
     * @param url
     * @param token
     * @param params
     * @param listener
     */
    public static void delete(final String url, final String token, final String params, final OnListener listener) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<String> emitter) {
                Log.i(TAG, "delete 请求接口：" + url +" 参数：" + params + " token：" + token);
                Request.Builder builder = new Request.Builder();
                if(!TextUtils.isEmpty(token)){
                    builder.addHeader("Authorization",token);
                }
                RequestBody requestBody = RequestBody.create(JSON,params);
                Request getReq = builder.url(url).delete(requestBody).build();
                httpClient.newCall(getReq).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i(TAG, "delete 请求失败: "+e.getMessage()+","+e.getCause());
                        emitter.onError(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String s = response.body().string();
                        Log.i(TAG, "delete 请求成功：" + s);
                        emitter.onNext(s);
                        emitter.onComplete();
                    }
                });
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull String s) {
                listener.onOk(s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                if(e instanceof SocketTimeoutException){
                }else if(e instanceof ConnectException){
                }else if(e instanceof UnknownHostException){
                }
                listener.onFail(e);
            }

            @Override
            public void onComplete() {

            }
        });

    }
}
