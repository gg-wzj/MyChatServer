package com.example.mychatserver.cotroller;

import com.example.mychatserver.TypeConstant;
import com.example.mychatserver.bean.NetBean;
import com.example.mychatserver.bean.SearchBean;
import com.example.mychatserver.db.bean.FriendshipBean;
import com.example.mychatserver.db.bean.UserBean;
import com.example.mychatserver.db.dao.UserDao;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.WriteAbortedException;
import java.net.Socket;

/**
 * Created by wzj on 2017/9/25.
 */

public class SearchHandler implements RequestHandler {

    private String request;
    private Socket mSocket;

    public SearchHandler(String requset, Socket socket) {
        this.request = requset;
        mSocket = socket;
    }

    @Override
    public void handleRequest() {
        String json = "";
        //解析收到的数据，获取返回的数据
        try {
            JSONObject jsonObject = new JSONObject(request);
            String phone = jsonObject.optString("data");

            UserDao dao = new UserDao();
            UserBean bean = dao.findWithPhone(phone);

            if (bean != null) {
                SearchBean searchBean = new SearchBean(phone, bean.getArea(), bean.getNick(), bean.getSex());
                NetBean<SearchBean> netBean = new NetBean<>(TypeConstant.RESPOND_SEARCH, searchBean);
                json = new Gson().toJson(netBean, NetBean.class);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            OutputStreamWriter opw = new OutputStreamWriter(mSocket.getOutputStream());
            BufferedWriter writer = new BufferedWriter(opw);
            writer.write(json);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
