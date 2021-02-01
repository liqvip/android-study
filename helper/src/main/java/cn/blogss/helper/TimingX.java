package cn.blogss.helper;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

/**
 * @Author: Thatcher Li
 * @Date: 2021/2/1
 * @LastEditors: Thatcher Li
 * @LastEditTime: 2021/2/1
 * @Descripttion: 多组件同步计时，Handler 实现
 */
public class TimingX {
    private volatile static TimingX sTimingX;

    private static int sSecond = 0, sStatus = TimingEnum.STOP.val;

    private static final Handler sHandler = new TimingXHandler();

    private static final ArrayList<TextView> timingViews = new ArrayList<>(8);

    private static String time = "";

    private TimingX() {
    }

    public static TimingX builder(){
        if(sTimingX == null){
            synchronized (TimingX.class){
                if(sTimingX == null){
                    sTimingX = new TimingX();
                }
            }
        }
        return sTimingX;
    }

    private static class TimingXHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            int status = msg.what;
            if (status == TimingEnum.START.val) {
                sSecond += 1;
                time = format(sSecond);
                sHandler.sendEmptyMessageDelayed(status, 1000);
            } else if (status == TimingEnum.STOP.val) {
                time = format(sSecond);
            }
            showTime();
        }
    }

    private static void showTime(){
        for (TextView timingView : timingViews) {
            timingView.setText(time);
        }
    }

    public int getStatus(){
        return sStatus;
    }

    enum TimingEnum {
        STOP(0), // 停止计时
        START(1), // 开始计时
        CLEAR(-1); // 清除计时
        int val;
        TimingEnum(int val){
            this.val = val;
        }
    }

    /**
     * 添加需要计时的 view
     * @param view
     * @return
     */
    public TimingX add(TextView view){
        if(!timingViews.contains(view)){
            timingViews.add(view);
        }
        return sTimingX;
    }

    /**
     * 移除不需要计时的 view
     * @param view
     * @return
     */
    public TimingX remove(TextView view){
        timingViews.remove(view);
        return sTimingX;
    }

    /**
     * 开始计时
     */
    public void start(){
        if(sStatus == TimingEnum.STOP.val){
            sStatus = TimingEnum.START.val;
            sHandler.sendEmptyMessageDelayed(sStatus, 1000);
        }
    }

    /**
     * 停止计时
     */
    public void stop(){
        if(sStatus == TimingEnum.START.val){
            sStatus = TimingEnum.STOP.val;
            sHandler.removeMessages(TimingEnum.START.val);
            sHandler.sendEmptyMessage(sStatus);
        }
    }

    /**
     * 销毁 handler
     */
    public void destroy(){
        sStatus = TimingEnum.STOP.val;
        sSecond = 0;
        sHandler.removeMessages(TimingEnum.START.val);
        timingViews.clear();
    }

    /**
     * 将秒转化为分秒的形式(00:00)
     * @param second
     * @return
     */
    public static String format(int second) {
        StringBuilder sb = new StringBuilder();
        int minutes = second / 60;
        int sec = second % 60;
        if(minutes < 10){
            sb.append(0);
        }
        sb.append(minutes);
        sb.append(":");
        if(sec < 10){
            sb.append(0);
        }
        sb.append(sec);
        return sb.toString();
    }
}
