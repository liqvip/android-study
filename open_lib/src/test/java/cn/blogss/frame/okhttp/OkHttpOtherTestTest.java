package cn.blogss.frame.okhttp;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.URISyntaxException;

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
}