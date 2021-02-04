package cn.blogss.core.ipc.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Thatcher Li
 * @Date: 2021/2/3
 * @LastEditors: Thatcher Li
 * @LastEditTime: 2021/2/3
 * @Descripttion: 远程服务端 Service 的实现，跨进程通信。
 * 本例使用观察者模式，作为主题，可以向观察者定时推送新书本
 * 的信息，
 */
public class BookManagerService extends Service {
    private static final String TAG = "BookManagerService";

    private AtomicBoolean mIsServiceDestroyed = new AtomicBoolean(false);

    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();

    private RemoteCallbackList<IBookObserver> mBookObservers = new RemoteCallbackList<>();

    private IBookManager.Stub mBinder = new IBookManager.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        /**
         * 服务端的方法运行在服务端的 Binder 线程池中，所以本身就可以执行大量耗时的操作
         * 注意客户端调用的时候，如果这个方法很耗时，最好开一个线程调用，否则会导致客户端出现 ANR
         * @return
         * @throws RemoteException
         */
        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }

        @Override
        public void registerBookObserver(IBookObserver observer) throws RemoteException {
            mBookObservers.register(observer);
            Log.i(TAG, "register successful, current size: "+mBookObservers.beginBroadcast());
            mBookObservers.finishBroadcast();
        }

        @Override
        public void removeBookObserver(IBookObserver observer) throws RemoteException {
            mBookObservers.unregister(observer);
            Log.i(TAG, "unregister observer, current size: "+mBookObservers.beginBroadcast());
            mBookObservers.finishBroadcast();
        }

        @Override
        public void notifyObservers(Book book) throws RemoteException {
            mBookList.add(book);
            final int n = mBookObservers.beginBroadcast();
            for (int i=0; i< n;i++){
                // 如果客户端的这个方法比较耗时，那么确保 notifyObservers 运行在非 UI 线程中，本例运行在非 UI 线程。否则将导致服务端无法响应
                mBookObservers.getBroadcastItem(i).onNewBookArrived(book);
            }
            mBookObservers.finishBroadcast();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1, "Android"));
        mBookList.add(new Book(2, "IOS"));

        // 每隔 5s 向客户端推送新书的信息
        new Thread(new ServiceWorker()).start();
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private class ServiceWorker implements Runnable {
        @Override
        public void run() {
            while(!mIsServiceDestroyed.get()){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                int bookId = mBookList.size() + 1;
                Book newBook = new Book(bookId,"new book#"+bookId);
                try {
                    mBinder.notifyObservers(newBook);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIsServiceDestroyed.set(true);
    }
}
