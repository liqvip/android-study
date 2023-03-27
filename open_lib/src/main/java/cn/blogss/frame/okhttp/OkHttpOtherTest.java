package cn.blogss.frame.okhttp;

import java.net.InetAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;

import okhttp3.OkHttpClient;

public class OkHttpOtherTest {
    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .build();


    public void printProxyList() throws URISyntaxException {
        Proxy proxy = okHttpClient.proxy();
        if (proxy == null) {
            System.out.println("OkHttpClient proxy is null");
        }

        ProxySelector proxySelector  = okHttpClient.proxySelector();
        List<Proxy> proxies  = proxySelector.select(new URI("https://baidu.com"));

        Iterator<Proxy> proxyIterator = proxies.iterator();
        while (proxyIterator.hasNext()) {
            Proxy it = proxyIterator.next();
            System.out.println(it + "\n");
        }
    }

    public void domainResolution() throws UnknownHostException {
        InetAddress[] inetAddresses = InetAddress.getAllByName("baidu.com");
        for (InetAddress inetAddress : inetAddresses) {
            System.out.println(inetAddress.toString());
        }
    }
}
