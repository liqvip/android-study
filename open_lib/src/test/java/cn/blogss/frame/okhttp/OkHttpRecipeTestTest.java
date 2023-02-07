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

    @Test
    public void postStreaming() {
        try {
            okHttpRecipeTest.postStreaming();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void postFile() {
        try {
            okHttpRecipeTest.postFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void postForm() {
        try {
            okHttpRecipeTest.postForm();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void postMultipartBody() {
        try {
            okHttpRecipeTest.postMultipartBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void moshiParse() {
        try {
            okHttpRecipeTest.moshiParse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void cacheResponse() {
        try {
            okHttpRecipeTest.cacheResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void cancelCall() {
        try {
            okHttpRecipeTest.cancelCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void timeout() {
        try {
            okHttpRecipeTest.timeout();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void perCallConfiguration() {
        try {
            okHttpRecipeTest.perCallConfiguration();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void authenticate() {
        try {
            okHttpRecipeTest.authenticate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}