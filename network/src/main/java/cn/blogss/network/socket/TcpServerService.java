package cn.blogss.network.socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServerService extends Service {
    private static final String TAG = "TcpServer";

    private static boolean disconnected = false;

    private static Socket client;

    
    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new TcpServe()).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
        disconnected = true;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");
        return null;
    }

    private static class TcpServe implements Runnable {
        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(50001);
                Log.i(TAG, "TCP 服务已创建");
            } catch (Exception e){
                e.printStackTrace();
                Log.i(TAG, "Tcp 服务创建失败");
                return;
            }

            while (!disconnected){
                try {
                    client = serverSocket.accept(); // 阻塞
                    new Thread(new Response()).start();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private static class Response implements Runnable {
        @Override
        public void run() {
            try {
                //接受消息
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                //回复消息
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
                out.println("服务端已连接 *****");

                while (!disconnected) {
                    String inputStr = in.readLine();    // 阻塞
                    out.println("你这句【" + inputStr + "】非常有道理啊！");
                }
                out.close();
                in.close();
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
