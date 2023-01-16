package cn.blogss.frame.okio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InterruptedIOException;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;
import okio.Source;
import okio.Timeout;


public class OkioTest {
    public void readFile() {
        try {
            FileInputStream fis = new FileInputStream("test.txt");
            Source source = Okio.source(fis);
            BufferedSource bSource = Okio.buffer(source);
            String str = bSource.readUtf8();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readAndWrite() {
        try {
            FileInputStream fis = new FileInputStream("test.txt");
            Source source = Okio.source(fis);
            BufferedSource bSource = Okio.buffer(source);

            FileOutputStream fos = new FileOutputStream("test2.txt");
            Sink sink = Okio.sink(fos);
            BufferedSink bSink = Okio.buffer(sink);

            while (!bSource.exhausted()){
                bSource.read(bSink.buffer(), 8*1024);
                bSink.emit();
            }

            bSource.close();
            bSink.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deadline(){
        try {
            FileInputStream fis = new FileInputStream("test.txt");
            okio.Source source = Okio.source(fis);
            BufferedSource bs = Okio.buffer(source);
            source.timeout().deadline(1, TimeUnit.MILLISECONDS);
            String res = bs.readUtf8();
            System.out.println(res);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 在JUnit的@Test方法中启用多线程，新启动的线程会随着@Test主线程的死亡而不输出
     * 在@Test方法中每创建一个线程，就join一下，这样我们新建的线程不死亡，Test主线程也不会死亡
     */
    public void deadlineForThread(){
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    FileInputStream fis = new FileInputStream("test.txt");
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

    public void timeout(){
        try {
            Dice dice = new Dice();
            dice.rollAtFixedRate(3, TimeUnit.SECONDS);
            Timeout timeout = new Timeout();
//            timeout.timeout(5, TimeUnit.SECONDS);
            timeout.deadline(6, TimeUnit.SECONDS);
            dice.awaitTotal(timeout, 20);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class Dice {
        Random random = new Random();
        int latestTotal;

        public synchronized void roll() {
            latestTotal = 2 + random.nextInt(6) + random.nextInt(6);
            System.out.println("Rolled " + latestTotal);
            notifyAll();
        }

       public void rollAtFixedRate(int period, TimeUnit timeUnit) {
          Executors.newScheduledThreadPool(0).scheduleAtFixedRate(new Runnable() {
            public void run() {
              roll();
             }
          }, 0, period, timeUnit);
        }

        public synchronized void awaitTotal(Timeout timeout, int total) throws InterruptedIOException {
          while (latestTotal != total) {
            timeout.waitUntilNotified(this);
          }
       }
    }

    public void getPath(){
        System.out.println(System.getProperty("user.dir"));
    }
}
