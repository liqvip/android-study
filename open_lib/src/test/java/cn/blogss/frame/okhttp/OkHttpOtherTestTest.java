package cn.blogss.frame.okhttp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.URISyntaxException;
import java.net.UnknownHostException;

public class OkHttpOtherTestTest {
    OkHttpOtherTest okHttpOtherTest;

    @Before
    public void setUp() throws Exception {
        okHttpOtherTest = new OkHttpOtherTest();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void printProxyList() {
        try {
            okHttpOtherTest.printProxyList();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void domainResolution() {
        try {
            okHttpOtherTest.domainResolution();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}