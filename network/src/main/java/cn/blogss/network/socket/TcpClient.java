package cn.blogss.network.socket;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Note:
 * 1. 文件流可以很清楚的知道流的结尾，但是 socket 流不一样，你无法知道它什么时候到达流的结尾，所以 socket 流的
 * read 系列方法会一直保持阻塞，并不会返回 -1 或者 null ，除非这个 socket 流被关闭了
 *
 * 2. 基于以上的原因，在读取数据时，最好一次性使用字节流或字符流读取完整的数据
 *
 * 3. 如果不想 socket 流的 read 系列方法一直阻塞，可以使用 setSoTimeout() 方法设置读取的超时时间
 * 如果 read 系列方法在设置时间内没有读取到数据，就会抛出一个java.net.SocketTimeoutException异常
 * 注意这个方法必须在阻塞发生之前设置，否则无效
*/
public class TcpClient {

    private final static String TAG = TcpClient.class.getSimpleName();

    private static volatile TcpClient client = null;
    private volatile boolean connected = false;
    private static final String mServerIP = "127.0.0.1";
    private static final int mServerPort = 50001;
    private Socket mSocket = null;
    private InputStream socketIs;
    private OutputStream socketOs;
    private BufferedOutputStream writeStream;
    private BufferedInputStream readStream;

    public MutableLiveData<String> getReceiveMsg() {
        return receiveMsg;
    }

    private final MutableLiveData<String> receiveMsg = new MutableLiveData<>();

    public static TcpClient getInstance() {
        if (client == null) {
            synchronized (TcpClient.class) {
                if(client == null){
                    client = new TcpClient();
                }
            }
        }
        return client;
    }

    public static final int TCP_CONN_FAIL = -1; //连接失败
    public static final int TCP_TIMEOUT_EXP = -2;   // 连接超时
    public static final int TCP_CONN_SUCCESS = 1;   //连接成功
    public static final int TCP_RECEIVE_OK = 2; //报文接收成功

    public void server(){
        mHandler.postDelayed(connectTask,3*1000);
    }

    /**
     * 连接主机的task
     */
    private final Runnable connectTask = new Runnable() {
        @Override
        public void run() {
            if(!connected()){
                new Thread(() -> connectServer()).start();
            }
            mHandler.postDelayed(connectTask,5*1000);
        }
    };

    private synchronized void connectServer() {
        try {
            if (mSocket != null) {
                mSocket.close();
                mSocket = null;
            }
            mSocket = new Socket();
            mSocket.setKeepAlive(true);
            mSocket.setReuseAddress(true);

            if(!mSocket.isConnected()){
                Log.i(TAG,"开始连接 Tcp 服务端, Ip: " + mServerIP + ", Port: " + mServerPort);
                long connStartTime = System.currentTimeMillis();
                mSocket.connect(new InetSocketAddress(mServerIP, mServerPort), 2 * 1000);
                Log.i(TAG, "连接 Tcp 服务端成功，耗时：" + (System.currentTimeMillis()-connStartTime) + "ms");

                mSocket.setTcpNoDelay(false);
                socketIs = mSocket.getInputStream();
                socketOs = mSocket.getOutputStream();
                readStream = new BufferedInputStream(socketIs);
                writeStream = new BufferedOutputStream(socketOs);
                mHandler.sendEmptyMessage(TCP_CONN_SUCCESS);
            }
        } catch (Exception e) {
            Log.i(TAG, "连接 Tcp 服务端失败");
            e.printStackTrace();
            Message message = mHandler.obtainMessage();
            message.what = TCP_CONN_FAIL;
            message.obj = e;
            mHandler.sendMessage(message);
        }
    }


    /**
     * 报文接收线程
     */
    class ReceiveThread implements Runnable {
        @Override
        public void run() {
            byte[] readData = new byte[1024*8]; // 一次性最多读取 8kb，数据量大于这个值的时候，只会读取到其中的一部分
            while (connected()){
                try {
                    int len = readStream.read(readData); // 阻塞
                    if (len > 0) {
                        Message msg = Message.obtain();
                        msg.what = TCP_RECEIVE_OK;
                        msg.obj = new String(readData, 0, len, StandardCharsets.UTF_8);
                        mHandler.sendMessage(msg);
                    } else if(len < 0) {
                        Log.i(TAG, "Socket 异常关闭");
                        mHandler.sendEmptyMessage(TCP_TIMEOUT_EXP);
                        disconnect();
                        break;
                    }
                } catch (Exception e) {
                    Log.i(TAG, "读取线程发生异常: "+e.getCause()+","+e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case TCP_CONN_SUCCESS:
                    connected = true;
                    new Thread(new ReceiveThread()).start();
                    break;
                case TCP_RECEIVE_OK:
                    String data = msg.obj.toString();
                    Log.i(TAG, "收到服务端的消息：" + data);
                    receiveMsg.setValue(data);
                    break;
                case TCP_TIMEOUT_EXP:
                    disconnect();
                    break;
                case TCP_CONN_FAIL:
                    break;
            }

            return false;
        }
    });


    /**
     * 向 Tcp 服务端发送数据
     * @param msg 发送的数据
     */
    public void sendMsg(String msg)  {
        new Thread(){
            @Override
            public void run() {
                try {
                    writeStream.write(msg.getBytes(StandardCharsets.UTF_8));
                    writeStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    private synchronized boolean connected(){
        return (connected && mSocket != null && mSocket.isConnected());
    }

    /**
     * 关闭 socket 连接
     */
    public void disconnect() {
        connected = false;
        try {
            if(mSocket != null && mSocket.isConnected()){
                if (socketIs != null) {
                    socketIs.close();
                }
                if (socketOs != null) {
                    socketOs.close();
                }
            }
            if (mSocket != null) {
                mSocket.close();
                mSocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
