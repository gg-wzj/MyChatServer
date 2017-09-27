package com.example.mychatserver.cotroller;

import android.util.Log;

import com.example.mychatserver.MyChatService;
import com.example.mychatserver.TypeConstant;
import com.example.mychatserver.bean.LoginBean;
import com.example.mychatserver.bean.NetBean;
import com.example.mychatserver.db.bean.UserBean;
import com.example.mychatserver.db.dao.UserDao;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.Socket;

/**
 * Created by wzj on 2017/9/24.
 */

public class LoginHandler implements RequestHandler {


    private  String request;
    private  Socket mSocket;

    public LoginHandler(String request, Socket socket) {
        this.request = request;
        mSocket = socket;
    }

    @Override
    public void handleRequest() {
        //解析获取的json数据
        Gson gson = new Gson();
        Type type = new TypeToken<NetBean<LoginBean>>() {
        }.getType();
        NetBean<LoginBean> login = gson.fromJson(request, type);
        LoginBean data = login.getData();

        UserDao dao = new UserDao();
        UserBean user = dao.findWithPhone(data.getPhone());


        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("type",TypeConstant.RESPOND_LOGIN);
            if(user == null ){
                //数据库没找到该账号
                jsonObject.put("data","fail");
            }else if(!user.getPwd().equals(data.getPwd())) {
                //密码错误
                jsonObject.put("data","fail");
            }else {
                //登录成功
                jsonObject.put("data","ok");
                //向onlineUser 中加入该用户
                try {
                    OutputStreamWriter opw = new OutputStreamWriter(mSocket.getOutputStream());
                    BufferedWriter writer = new BufferedWriter(opw);
                    MyChatService.onlineUser.put(data.getPhone(),writer);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            OutputStreamWriter opw = new OutputStreamWriter(mSocket.getOutputStream());
            BufferedWriter writer = new BufferedWriter(opw);
            writer.write(jsonObject.toString());
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
