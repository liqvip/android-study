package cn.blogss.frame.okhttp;

import static org.junit.Assert.*;

import org.checkerframework.checker.units.qual.C;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class ChatGPTTest {
    private ChatGPT chatGPT;

    @Before
    public void setUp() throws Exception {
        chatGPT = new ChatGPT();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void completion() {
        try {
            chatGPT.completion("以我的妈妈为题写一篇500字文章");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}