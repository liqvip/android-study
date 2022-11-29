package cn.blogss.frame.okio;

import java.io.FileInputStream;

import okio.BufferedSource;
import okio.Okio;


public class Source {

    public void readFile() {
        try {
            FileInputStream fis = new FileInputStream("C:\\Users\\liqiang2.ZHUT\\Desktop\\test.txt");
            okio.Source source = Okio.source(fis);
            BufferedSource bs = Okio.buffer(source);
            String res = bs.readUtf8();
            System.out.println(res);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
