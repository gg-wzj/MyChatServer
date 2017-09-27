package com.example.mychatserver.cotroller;

import android.util.Log;

import com.example.mychatserver.bean.NetBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.Socket;

/**
 * Created by wzj on 2017/9/25.
 */

public abstract class BaseHandler<T> implements RequestHandler {

    private String request;
    private Socket mSocket;
    private T data;


    public BaseHandler(String request, Socket socket) {
        this.request = request;
        mSocket = socket;
    }

    @Override
    public void handleRequest() {
        Gson gson = new Gson();
        Type type = new TypeToken<NetBean<T>>() {
        }.getType();
        NetBean<T> netBean = gson.fromJson(request, type);
        data = netBean.getData();
        handleData(data,mSocket);
    }

    public String getRequest() {
        return request;
    }

    public Socket getmSocket() {
        return mSocket;
    }

    public T getData() {
        return data;
    }

    abstract void handleData(T data,Socket socket);
}
