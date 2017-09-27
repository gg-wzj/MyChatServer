package com.example.mychatserver.cotroller;


import android.util.Log;

import com.example.mychatserver.TypeConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.Socket;

/**
 * Created by wzj on 2017/9/23.
 */

public class RequestManager {

    private String request;
    Socket msocket;

    public RequestManager(String request, Socket s) {
        this.request = request;
        msocket = s;
    }

    public void dispatchRequest() {
        try {
            JSONObject jsonObject = new JSONObject(request);
            String type = jsonObject.optString("type");
            RequestHandler handler = null;
            switch (type) {
                case TypeConstant.REQUSET_REGISTER:
                    handler = new RegiterHandler(request, msocket);
                    break;
                case TypeConstant.REQUSET_LOGIN:
                    handler = new LoginHandler(request,msocket);
                    break;
                case TypeConstant.REQUEST_SENDMESSAGE:
                    handler = new MessageHandler(request,msocket);
                    break;
                case TypeConstant.REQUEST_SEARCH:
                    handler = new SearchHandler(request,msocket);
                    break;
                case TypeConstant.REQUEST_VERIFICATION:
                    handler = new VerificationHandler(request,msocket);
                    break;
                case TypeConstant.HANDLE_VERIFICATION:
                    handler = new VerificationResultHandler(request,msocket);
                    break;
            }
            if (handler != null)
                handler.handleRequest();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
