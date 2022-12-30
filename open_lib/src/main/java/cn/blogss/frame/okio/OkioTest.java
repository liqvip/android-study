package cn.blogss.frame.okio;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;
import okio.Source;


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

    public void getPath(){
        System.out.println(System.getProperty("user.dir"));
    }
}
