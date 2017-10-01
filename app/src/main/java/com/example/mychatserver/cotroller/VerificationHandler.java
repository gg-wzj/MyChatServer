package com.example.mychatserver.cotroller;

import com.example.mychatserver.MyChatService;
import com.example.mychatserver.TypeConstant;
import com.example.mychatserver.bean.NetBean;
import com.example.mychatserver.bean.SendMessageBean;
import com.example.mychatserver.db.bean.FriendshipBean;
import com.example.mychatserver.db.bean.OfflineBean;
import com.example.mychatserver.db.bean.UserBean;
import com.example.mychatserver.db.dao.FriendshipDao;
import com.example.mychatserver.db.dao.OfflineDao;
import com.example.mychatserver.db.dao.UserDao;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.Socket;

/**
 * Created by wzj on 2017/9/26.
 */

public class VerificationHandler implements RequestHandler {

    private String request;
    private final Socket mSocket;

    public VerificationHandler(String request, Socket socket) {
        this.request = request;
        mSocket = socket;
    }


    @Override
    public void handleRequest() {
        OfflineBean data = getData();

        handleData(data);
    }

    private void handleData(OfflineBean data) {
        String toUser = data.getToUser();
        BufferedWriter writer = MyChatService.onlineUser.get(toUser);
        String message = data.getMessage();
        String trueMessage;
        trueMessage = message.substring(0,message.lastIndexOf("/"));

        String remark;
        UserDao u_dao  = new UserDao();
        if (message.lastIndexOf("/") == message.length()) {
            //说明没有备注
            //备注为用户的nick
            remark = u_dao.findWithPhone(data.getToUser()).getNick();
        } else {
            //有备注 获取备注
            remark = message.substring(message.lastIndexOf("/")+1);
        }


        UserBean userBean = u_dao.findWithPhone(data.getFromUser());
        String nick = userBean.getNick();
        String str = nick+"/"+ trueMessage;
        SendMessageBean sendMessageBean = new SendMessageBean(data.getFromUser(), 2, str, 1, data.getDate());
        NetBean<SendMessageBean> netBean = new NetBean<>(TypeConstant.RECEIVE_VERIFICATION, sendMessageBean);
        String json = new Gson().toJson(netBean, NetBean.class);
        if (writer != null) {

            try {
                writer.write(json);
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //保存到offline表中
            OfflineDao dao = new OfflineDao();
            data.setMessage(json);
            dao.addOffline(data);

        }

        //保存到friendship表中
        FriendshipDao f_dao = new FriendshipDao();
        FriendshipBean friendshipBean = new FriendshipBean(data.getFromUser(), data.getToUser(), 0, remark,userBean.getNick(),data.getDate());
        f_dao.addFriendship(friendshipBean);


        //反馈给用户 说明好友申请已经发送
        try {
            OutputStreamWriter opw = new OutputStreamWriter(mSocket.getOutputStream());
            BufferedWriter writer1 = new BufferedWriter(opw);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", TypeConstant.RESPOND_VERIFICATION);
            jsonObject.put("data", "ok");
            String s = jsonObject.toString();
            writer1.write(s);
            writer1.newLine();
            writer1.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private OfflineBean getData() {
        Gson gson = new Gson();
        Type type = new TypeToken<NetBean<OfflineBean>>() {
        }.getType();
        NetBean<OfflineBean> netBean = gson.fromJson(request, type);
        return netBean.getData();
    }
}
