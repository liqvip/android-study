package cn.blogss.helper;

import android.content.Context;

/**
 * @创建人 560266
 * @文件描述 获取资源工具类
 * @创建时间 2020/4/29
 */
public class ResUtil {

    /**
     * 获取字符串
     * @param context
     * @param id
     * @return
     */
    public static String getStr(Context context,int id){
        return context.getResources().getString(id);
    }

    /**
     * 获取字符串数组
     * @param context
     * @param id
     * @return
     */
    public static String[] getStrArr(Context context,int id){
        return context.getResources().getStringArray(id);
    }

    /**
     * 根据资源的名字获取其对应的ID
     * @param context
     * @param name，资源的名字
     * @param type，资源的类型，可传入("string","array","drawable")
     * @return
     */
    public static int getId(Context context, String name, String type) {
        int id = 0;
        try {
            id = context.getResources().getIdentifier(name, type, context.getPackageName());
        } catch (Exception e) {
        }
        return id;
    }

    /**
     * 根据资源的名字获取资源数组
     * @param context
     * @param name，资源名字数组
     * @param type，资源类型
     * @return
     */
    public static int[] getIds(Context context, String[] name, String type){
        int[] ids = new int[name.length];
        for(int i=0;i<name.length;i++){
            try {
                int id = context.getResources().getIdentifier(name[i], type, context.getPackageName());
                ids[i] = id;
            } catch (Exception e){

            }
        }
        return ids;
    }
}
