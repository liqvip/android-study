package cn.blogss.network.socket;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NetBaseTest {
    NetBase netBase;

    @Before
    public void setUp() throws Exception {
        netBase = new NetBase();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void printAllNetworkInterfaceInfo() {
        netBase.printAllNetworkInterfaceInfo();
    }

    @Test
    public void connectByBind() {
        System.out.println(netBase.connectByBind());
    }
}