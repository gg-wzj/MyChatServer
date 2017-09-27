package com.example.mychatserver.cotroller;


import com.example.mychatserver.MyChatService;
import com.example.mychatserver.TypeConstant;
import com.example.mychatserver.bean.NetBean;
import com.example.mychatserver.bean.SendMessageBean;
import com.example.mychatserver.db.bean.OfflineBean;
import com.example.mychatserver.db.dao.OfflineDao;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.Socket;

/**
 * Created by wzj on 2017/9/25.
 */

public class MessageHandler implements RequestHandler {


    private  Socket mSocket;
    private  String request;

    public MessageHandler(String request, Socket socket) {
        this.request = request;
        mSocket = socket;
    }

    @Override
    public void handleRequest() {
        Gson gson = new Gson();
        Type type = new TypeToken<NetBean<OfflineBean>>() {
        }.getType();
        NetBean<OfflineBean> netBean = gson.fromJson(request, type);
        OfflineBean data = netBean.getData();
        handleData(data,mSocket);
    }

    private void handleData(OfflineBean data, Socket socket) {
        String toUser = data.getToUser();
        BufferedWriter writer = MyChatService.onlineUser.get(toUser);

        SendMessageBean bean = new SendMessageBean(data.getFromUser(), 0, data.getMessage(), 1, System.currentTimeMillis());
        NetBean<SendMessageBean> netBean = new NetBean<>(TypeConstant.RECEIVE_MESSAGE, bean);

        String json = new Gson().toJson(netBean,NetBean.class);
        if (writer != null) {
            //用户在线 发送给用户

            try {
                writer.write(json);
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            //用户不在线 保存在离线数据库中
            OfflineDao dao = new OfflineDao();
            data.setMessage(json);
            dao.addOffline(data);
        }


        try {
            OutputStreamWriter opw  = new OutputStreamWriter(socket.getOutputStream());
            BufferedWriter writer1 = new BufferedWriter(opw);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type",TypeConstant.RESPOND_SENDMESSAGE);
            jsonObject.put("data","ok");
            String json1 = jsonObject.toString();
            writer1.write(json1);
            writer1.newLine();
            writer1.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
