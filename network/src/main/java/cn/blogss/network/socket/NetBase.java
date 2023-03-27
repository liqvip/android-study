package cn.blogss.network.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Enumeration;

public class NetBase {
    public void printAllNetworkInterfaceInfo(){
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface ni = networkInterfaces.nextElement();

                if(!isValidInterface(ni)) continue;

                int index = ni.getIndex();
                String name = ni.getName();
                String displayName = ni.getDisplayName();
                boolean up = ni.isUp();
                int mtu = ni.getMTU();
                String hardwareAddress = Arrays.toString(ni.getHardwareAddress());
                boolean loopback = ni.isLoopback();
                boolean virtual = ni.isVirtual();
                System.out.println("index:" + index + ", name:" + name + ", displayName:" + displayName + ", up:" + up + ", mtu:" + mtu + ", hardwareAddress:" + hardwareAddress + ", loopback:" + loopback + ", virtual:" + virtual);

                System.out.println("List of inetAddresses:");
                Enumeration<InetAddress> inetAddresses = ni.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();

                    if(!isValidAddress(inetAddress)) continue;

                    String hostName = inetAddress.getHostName();
                    String hostAddress = inetAddress.getHostAddress();
                    System.out.println("hostName:" + hostName + ", hostAddress:" + hostAddress);
                }

/*                System.out.println("List of interfaceAddresses:");
                List<InterfaceAddress> interfaceAddresses = ni.getInterfaceAddresses();
                for (InterfaceAddress item: interfaceAddresses) {
                    String address = item.getAddress().getHostAddress();
                    InetAddress broadcastAddress = item.getBroadcast();
                    String broadcast = broadcastAddress == null ? "null" : broadcastAddress.getHostAddress();
                    short networkPrefixLength = item.getNetworkPrefixLength();
                    System.out.println("address:" + address + ", broadcast:" + broadcast + ", networkPrefixLength:" + networkPrefixLength);
                }*/
                System.out.println("\n");
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public boolean connectByBind(){
        Socket socket = null;
        try {
            socket = new Socket();
            InetAddress bindInetAddress = getInetAddressByInterfaceName("eth3");
            System.out.println(bindInetAddress);
            if(bindInetAddress == null) return false;
            socket.bind(new InetSocketAddress(bindInetAddress, 0));
            socket.connect(new InetSocketAddress("www.baidu.com", 80));

            System.out.println("getRemoteSocketAddress: " + socket.getRemoteSocketAddress());

            /**
             * 写入 Http 报文(请求行，请求头)
             */
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write("GET / HTTP/1.1\r\n");
            bw.write("Host: www.baidu.com\r\n");
            bw.write("\r\n");
            bw.flush();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            char[] buf = new char[1024*8];
            int len;
            while (true) {
                len = bufferedReader.read(buf);
                System.out.println("len: " + len);
                if(len == -1) {
                    break;
                }
                System.out.println(new String(buf, 0, len));
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private InetAddress getInetAddressByInterfaceName(String name){
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface ni = networkInterfaces.nextElement();
                String interfaceName = ni.getName();
                if(!isValidInterface(ni)) continue;
                Enumeration<InetAddress> inetAddresses = ni.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if(!isValidAddress(inetAddress)) continue;
                    if(interfaceName.equals(name)) return inetAddress;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static boolean isValidInterface(NetworkInterface ni) throws SocketException {
        return !ni.isLoopback() && !ni.isPointToPoint() && ni.isUp() && !ni.isVirtual()
                && (ni.getName().startsWith("eth") || ni.getName().startsWith("wlan") || ni.getName().startsWith("ppp"));
    }

    private static boolean isValidAddress(InetAddress address) {
        return address instanceof Inet4Address && address.isSiteLocalAddress() && !address.isLoopbackAddress();
    }

}
