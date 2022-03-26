package cn.blogss.core.demo;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MainTestTest {
    private MainTest mainTest;

    @Before
    public void setUp() throws Exception {
        mainTest = new MainTest();
        System.out.println("dfdffffffffffffffffffffffff");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void serializable() {
        assertEquals(mainTest.serializable(), "haha");

    }
}