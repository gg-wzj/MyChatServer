package com.example.mychatserver.cotroller;

import com.example.mychatserver.MyChatService;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.Socket;

/**
 * Created by wzj on 2017/9/30.
 */

public class ExitHandler implements RequestHandler {

    private  String request;
    private  Socket mSocket;

    public ExitHandler(String request, Socket socket) {
        this.request = request;
        mSocket = socket;
    }

    @Override
    public void handleRequest() {
        try {
            JSONObject jsonObject = new JSONObject(request);
            String user = jsonObject.optString("data");
            MyChatService.onlineUser.remove(user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
