package cn.blogss.frame.rxjava

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import cn.blogss.frame.R
import cn.blogss.helper.base.BaseActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Action
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.internal.schedulers.IoScheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.lang.Exception
import java.lang.RuntimeException

class RxJavaActivity : BaseActivity(), View.OnClickListener {
    private lateinit var btException: Button

    companion object {
        private const val TAG = "RxJavaActivity"
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_rxjava
    }

    override fun initView() {
        btException = findViewById(R.id.bt_exception)
        btException.setOnClickListener(this)
        /**
         * 创建 Observer（观察者）
         */
        val observer = object: Observer<String>{
            override fun onComplete() {
                Log.i(TAG, "onComplete")
            }

            override fun onSubscribe(d: Disposable) {
                Log.i(TAG, "onSubscribe")
            }

            override fun onNext(t: String) {
                Log.i(TAG, "onNext: $t")
            }

            override fun onError(e: Throwable) {
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
            override fun accept(t: String) {
                Log.i(TAG, "accept: $t")
            }
        }

        val onErrorAction = object:Consumer<Throwable>{
            // onError()
            override fun accept(t: Throwable) {

            }

        }

        val onCompletedAction = object: Action{
            // onCompleted
            override fun run() {
                Log.i(TAG, "run: onCompleted()")
            }
        }

        observable1.subscribe(onNextAction)
        // 将会依次调用 accept("aaa")->accept("bbb")->accept("bbb")

        /**
         * 在 RxJava 的默认规则中，事件的发出和消费都是在同一个线程的。也就是说，如果只用上面的方法，实现出来的只是一个同步的观察者模式。
         * 观察者模式本身的目的就是『后台处理，前台回调』的异步机制，因此异步对于 RxJava 是至关重要的。
         * 而要实现异步，则需要用到 RxJava 的另一个概念： Scheduler 。
            Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> arg0) {
            callFum(arg0, url, param, type, header);
            arg0.onCompleted();
            }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable arg0) {
            }
            @Override
            public void onNext(String result) {
            requestAsynNextFun(result, url, param, type, header, listener, 2,firstTiemL);//增加重试次数
            }
            });
         */
        Observable.just(1,2,3,4)
                .subscribeOn(IoScheduler()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread())  // 指定观察者的回调发生在主线程
                .subscribe(object: Consumer<Int>{
                    override fun accept(t: Int) {
                        Log.i(TAG, "accept: $t")
                    }
                })

        Observable.create(object: ObservableOnSubscribe<String>{
            override fun subscribe(emitter: ObservableEmitter<String>) {
                emitter!!.onNext("aaa")
                emitter.onComplete()
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object: Observer<String>{
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: String) {
            }

            override fun onError(e: Throwable) {
            }
        })

        /**=====================变换=========================**/
        // map 变换，一对一，数据类型转换
        Observable.just("aaa")
                .map(object : Function<String,Int> {
                    override fun apply(t: String): Int {// 参数类型 String
                        return t!!.toInt()// 返回类型 Int
                    }
                })
                .subscribe(object : Observer<Int> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(t: Int) {

                    }

                    override fun onError(e: Throwable) {
                        Log.d(TAG, "onError: cast error !")
                    }

                })

        // flatMap 变换，一对多，将一个事件序列分为多个事件序列，然后将这些事件序列组合在一起发送给观察者
        val students = arrayOf(Student("liq", mutableListOf(Course("java"), Course("C"))))
        Observable.just(students[0])
                .flatMap(object : Function<Student,ObservableSource<Course>> {
                    override fun apply(t: Student): Observable<Course> {
                        return Observable.fromIterable(t!!.courses)
                    }
                })
                .subscribe(object : Consumer<Course> {
                    override fun accept(t: Course) {
                        Log.d(TAG, "accept: "+ t!!.name)
                    }
                })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTag(TAG)
        super.onCreate(savedInstanceState)
    }

    data class Student(var name:String, var courses:MutableList<Course>)

    data class Course(var name:String)

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.bt_exception -> {
                throw RuntimeException("测试用：这是一个自己主动抛出的异常")
            }
        }
    }
}
