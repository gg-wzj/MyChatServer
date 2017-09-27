package com.example.mychatserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.mychatserver.cotroller.RequestManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wzj on 2017/9/19.
 */

public class MyChatService extends Service {

    public static HashMap<String ,BufferedWriter> onlineUser = new HashMap<>();
    private ServerSocket serverSocket;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    boolean alive = false;

    @Override
    public void onCreate() {
        super.onCreate();
        alive = true;
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    serverSocket = new ServerSocket(30000);
                    while (alive) {
                        Socket client = serverSocket.accept();
                        Log.i("wzj", client.getInetAddress().getHostAddress());
                        new Thread(new ServerThread(client)).start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }


    class ServerThread implements Runnable {
        Socket s = null;

        public ServerThread(Socket client) {
            s = client;

        }

        @Override
        public void run() {
          StringBuilder builder = new StringBuilder();
            try {
               InputStreamReader isr = new InputStreamReader(s.getInputStream());
                BufferedReader reader = new BufferedReader(isr);
                String data ;
                while ((data = reader.readLine())!=null){
                    Log.e("Wzj",data);
                    RequestManager manager = new RequestManager(data,s);
                    manager.dispatchRequest();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        alive = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
