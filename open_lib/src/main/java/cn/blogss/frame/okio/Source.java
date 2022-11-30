package cn.blogss.frame.okio;

import java.io.FileInputStream;
import java.util.concurrent.TimeUnit;

import okio.BufferedSource;
import okio.Okio;
import okio.Timeout;


public class Source {

    public void readFile() {
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    FileInputStream fis = new FileInputStream("C:\\Users\\liqiang2.ZHUT\\Desktop\\test.txt");
                    okio.Source source = Okio.source(fis);
                    BufferedSource bs = Okio.buffer(source);
                    // 中断当前线程
                    interrupt();
                    String res = bs.readUtf8();
                    System.out.println(res);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        thread.start();

        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
