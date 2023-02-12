package cn.blogss.frame.okhttp;

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
            chatGPT.completion("你能背诵多少位圆周率");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testImageGeneration() {
        try {
            chatGPT.imageGeneration("Jay Chou plays the piano");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}