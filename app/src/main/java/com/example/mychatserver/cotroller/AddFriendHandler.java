package com.example.mychatserver.cotroller;

import java.net.Socket;

/**
 * Created by wzj on 2017/9/25.
 */

public class AddFriendHandler implements RequestHandler {

    private String request ;
    private Socket mSocket;

    public AddFriendHandler(String request , Socket socket) {
        this.request = request;
        mSocket = socket;
    }

    @Override
    public void handleRequest() {

    }
}
