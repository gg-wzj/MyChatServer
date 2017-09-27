package com.example.mychatserver.cotroller;


import android.util.Log;

import com.example.mychatserver.TypeConstant;
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
 * Created by wzj on 2017/9/23.
 */

public class RegiterHandler implements RequestHandler {

    private String request;
    private final Socket mSocket;

    public RegiterHandler(String request, Socket socket) {
        this.request = request;
        mSocket = socket;
    }

    @Override
    public void handleRequest() {
        //解析Json数据
        Type type = new TypeToken<NetBean<UserBean>>() {
        }.getType();
        NetBean<UserBean> register = new Gson().fromJson(request, type);
        UserBean data = register.getData();
        //查询数据库中是否已经存在
        UserDao dao = new UserDao();
        UserBean result = dao.findWithPhone(data.getPhone());
        String resultString = null;

        //不为空说明该已经存在 注册失败
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("type", TypeConstant.RESPOND_REGISTER);
            if (result != null) {
                jsonObject.put("data", "fail");
            } else {
                //注册成功 为数据库中添加该数据
                jsonObject.put("data", "ok");
                dao.addUser(data);
            }
            resultString = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            OutputStreamWriter ops = new OutputStreamWriter(mSocket.getOutputStream());
            BufferedWriter writer = new BufferedWriter(ops);
            //newLine和flush缺一不可 在数据小于默认buffer的时候一定要flush
            writer.write(resultString);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
