package cn.blogss.core.ipc.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Author: Thatcher Li
 * @Date: 2021/2/3
 * @LastEditors: Thatcher Li
 * @LastEditTime: 2021/2/3
 * @Descripttion:
 */
public class Book implements Parcelable {
    public int bookId;

    public String bookName;


    public Book(int bookId, String bookName) {
        this.bookId = bookId;
        this.bookName = bookName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(bookId);
        dest.writeString(bookName);
    }

    public static final Parcelable.Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    private Book(Parcel source) {
        bookId = source.readInt();
        bookName = source.readString();
    }
}
