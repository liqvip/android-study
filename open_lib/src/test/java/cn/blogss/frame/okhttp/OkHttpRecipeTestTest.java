package cn.blogss.frame.okhttp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class OkHttpRecipeTestTest {
    OkHttpRecipeTest okHttpRecipeTest;

    @Before
    public void setUp() throws Exception {
        okHttpRecipeTest = new OkHttpRecipeTest();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void synchronousGet() {
        try {
            okHttpRecipeTest.synchronousGet();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void asynchronousGet() {
        okHttpRecipeTest.asynchronousGet();
    }

    @Test
    public void accessHeaders() {
        try {
            okHttpRecipeTest.accessHeaders();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void postString() {
        try {
            okHttpRecipeTest.postString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}