package cn.blogss.frame.okio;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SourceTest {
    private Source source;

    @Before
    public void setUp() throws Exception {
        source = new Source();
    }

    @Test
    public void readFile() {
        source.readFile();
    }
}