package cn.blogss.kotlin.interop;

import androidx.annotation.Nullable;

import java.io.IOException;

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

        Spellbook spellbook = new Spellbook();
        for (String spell: spellbook.spells){
            System.out.println(spell);
        }

        System.out.println("Max spell count: " + Spellbook.MAX_SPELL_COUNT);

        Spellbook.getSpellbookGreeting();
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

    public void extendHandInFriendship() throws Exception {
        throw new Exception();
    }

    public void apologize() {
        try {
            Hero.acceptApology();
        } catch (IOException e){
            System.out.println("Caught");
        }
    }
}
