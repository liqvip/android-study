// IBookManager.aidl
package cn.blogss.core.ipc.aidl;

import cn.blogss.core.ipc.aidl.Book;
import cn.blogss.core.ipc.aidl.IBookObserver;

// Declare any non-default types here with import statements

interface IBookManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);


    List<Book> getBookList();

    void addBook(in Book book);

    void registerBookObserver(IBookObserver observer);

    void removeBookObserver(IBookObserver observer);

    void notifyObservers(in Book book);
}