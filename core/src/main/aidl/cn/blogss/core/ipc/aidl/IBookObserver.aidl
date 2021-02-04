// IBookObserver.aidl
package cn.blogss.core.ipc.aidl;

import cn.blogss.core.ipc.aidl.Book;

// Declare any non-default types here with import statements

interface IBookObserver {
    void onNewBookArrived(in Book book);
}