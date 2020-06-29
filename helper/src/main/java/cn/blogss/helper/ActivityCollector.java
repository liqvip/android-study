package cn.blogss.helper;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * @创建人 560266
 * @文件描述 活动管理类，用于收集所有生命周期未结束的活动
 * @创建时间 2020/6/29
 */
public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<>();

    public static void addAct(Activity activity) {
        activities.add(activity);
    }

    public static void removeAct(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAllAct() {
        for (Activity activity : activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
        activities.clear();
    }
}
