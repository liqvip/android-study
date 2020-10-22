package cn.blogss.frame.rxjava

import android.os.Bundle
import android.util.Log
import cn.blogss.core.base.BaseActivity
import cn.blogss.frame.R
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Action
import io.reactivex.rxjava3.functions.Consumer
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

class RxJavaActivity : BaseActivity() {
    companion object {
        private const val TAG = "RxJavaActivity"
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_rxjava
    }

    override fun initView() {
        /**
         * 创建 Observer（观察者）
         */
        val observer = object: Observer<String>{
            override fun onComplete() {
                Log.i(TAG, "onComplete")
            }

            override fun onSubscribe(d: Disposable?) {
                Log.i(TAG, "onSubscribe")
            }

            override fun onNext(t: String?) {
                Log.i(TAG, "onNext: $t")
            }

            override fun onError(e: Throwable?) {
                Log.i(TAG, "onError")
            }

        }

        val subscriber = object: Subscriber<String>{
            override fun onComplete() {

            }

            override fun onSubscribe(s: Subscription?) {

            }

            override fun onNext(t: String?) {
                Log.i(TAG, "onNext: $t")
            }

            override fun onError(t: Throwable?) {

            }

        }

        /**
         * 创建被观察者
         * ObservableOnSubscribe 会被存储在返回的 Observable 对象中，它的作用相当于一个计划表，
         * 当 Observable 被订阅的时候，ObservableOnSubscribe 的 subscribe() 方法会自动被调用。
         * 就实现了由被观察者向观察者的事件传递，即观察者模式。
         */
        val observable = Observable.create(object : ObservableOnSubscribe<String> {
            override fun subscribe(it: @NonNull ObservableEmitter<String>) {
                observer.onNext("aaa")
                observer.onNext("bbb")
                observer.onNext("ccc")
                observer.onComplete()
            }
        })

        /**
         * create() 方法是 RxJava 最基本的创造事件序列的方法。基于这个方法， RxJava 还提供了一些方法用来快捷创建事件队列，例如：
         * 1. just(T...): 将传入的参数依次发送出来
         * 2. fromArray(T... items): 将传入的数组或 Iterable 拆分成具体对象后，依次发送出来
         */
        val observable1 = Observable.just("aaa","bbb","ccc")
        //  将会依次调用 onSubscribe() -> onNext("aaa") -> onNext("bbb") -> onNext("ccc") -> onComplete()
        val words = arrayOf("a","b","c")
        val observable2 = Observable.fromArray(words)
        //  将会依次调用 onSubscribe() -> onNext("aaa") -> onNext("bbb") -> onNext("ccc") -> onComplete()

        /**
         * Subscribe (订阅)
         * 创建了 Observable 和 Observer 之后，再用 subscribe() 方法将它们联结起来，整条链子就可以工作了。代码形式很简单：
         */
        observable.subscribe(observer)
        observable1.subscribe(observer)
        //observable2.subscribe(observer)

        /**
         * subscribe() 还支持不完整定义的回调，RxJava 会自动根据定义创建出 Subscriber 。形式如下：
         */
        val onNextAction = object: Consumer<String>{
            //  onNext()
            override fun accept(t: String?) {

            }
        }

        val onErrorAction = object:Consumer<Throwable>{
            override fun accept(t: Throwable?) {
                TODO("Not yet implemented")
            }

        }

        val onCompletedAction = object: Action{
            // onCompleted
            override fun run() {

            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTag(TAG)
        super.onCreate(savedInstanceState)
    }
}