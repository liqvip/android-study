package cn.blogss.kotlin.interop;

import androidx.annotation.Nullable;

/**
 * @author: Little Bei
 * @Date: 2022/5/23
 */
public class Jhava {

    public int hitPoints = 52489112;

    public static void main(String[] args) {
        // Java 调用 Kotlin 函数
        //System.out.println(HeroKt.makeProclamation());
        System.out.println(Hero.makeProclamation());
    }

    public String utterGreeting(){
        return "BLARGH";
    }

    @Nullable
    public String determineFriendshipLevel(){
        return null;
    }

    public void offerFood(){
        Hero.handOverFood("pizza");
    }
}
