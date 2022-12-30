package cn.blogss.frame.okio;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OkioTestTest {
    private OkioTest okioTest;

    @Before
    public void setUp() throws Exception {
        okioTest = new OkioTest();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void readFile() {
        okioTest.readFile();
    }

    @Test
    public void readAndWrite() {
        okioTest.readAndWrite();
    }

    @Test
    public void getPath() {
        okioTest.getPath();
    }
}