package cn.blogss.core.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @创建人 560266
 * @文件描述 自己定义的一个内容提供器
 * @创建时间 2020/7/15
 */
class MyProvider extends ContentProvider {
    /*自定义 URI code 参数，调用 uriMatcher.match(uri) 匹配成功会返回对应的 code 值，如果没有匹配成功，返回 -1(UriMatcher.NO_MATCH)*/
    public static final int TABLE1_DIR = 0;

    public static final int TABLE1_ITEM = 1;

    public static final int TABLE2_DIR = 2;

    public static final int TABLE2_ITEM = 3;

    private static UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("cn.blogss.android_study", "table1", TABLE1_DIR);
        uriMatcher.addURI("cn.blogss.android_study", "table1/#", TABLE1_ITEM);
        uriMatcher.addURI("cn.blogss.android_study", "table2", TABLE2_DIR);
        uriMatcher.addURI("cn.blogss.android_study", "table2/#", TABLE2_ITEM);
    }

    /*初始化内容提供器的时候调用，通常会在这里完成对数据库的创建和升级等操作，返回true表示内容提供器初始化成功*/
    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        switch (uriMatcher.match(uri)){
            case TABLE1_DIR:
                /*查询 table1 表中所有数据*/
                break;
            case TABLE1_ITEM:
                /*查询 table1 表中的单条数据*/
                break;
            case TABLE2_DIR:
                /*查询 table2 表中所有数据*/
                break;
            case TABLE2_ITEM:
                /*查询 table2 表中的单条数据*/
                break;
            default:
                break;
        }
        return null;
    }

    /**
     * 根据传入的内容 URI 返回相应的 MIME 类型
     *
     * 一个标准的内容 URI 写法是这样的：content://cn.blogss.android_study/table1
     * 这就表示调用方期望访问的是 cn.blogss.android_study 这个应用的 table1 表中的数据
     * content://cn.blogss.android_study/table1/1, 调用方期望访问这个应用 table1 表中 id 为 1 的数据
     * content://cn.blogss.android_study/*, 一个能匹配任意表的内容 URI, (*表示匹配任意长度的任意字符)
     * content://cn.blogss.android_study/table1/#, 一个能匹配 table1 表中任意一行数据的的内容 URI, (#表示匹配任意长度的数字)
     *
     * 一个 URI 所对应的 MIME 字符串主要由 3 部分组成，Android 对这 3 部分做了如下的规定：
     * 1. 必须以 vnd 开头
     * 2. 如果 URI 以路径结尾，则后接 android.cursor.dir/，如果内容 URI 以 id 结尾，则后接 android.cursor.item/
     * 3.最后加上 vnd.<authority>.<path>
     * @param uri
     * @return
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)){
            case TABLE1_DIR:
                return "vnd.android.cursor.dir/vnd.cn.blogss.android_study.table1";
            case TABLE1_ITEM:
                return "vnd.android.cursor.item/vnd.cn.blogss.android_study.table1";
            case TABLE2_DIR:
                return "vnd.android.cursor.dir/vnd.cn.blogss.android_study.table2";
            case TABLE2_ITEM:
                return "vnd.android.cursor.item/vnd.cn.blogss.android_study.table2";
            default:
                break;
        }
        return "";
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
