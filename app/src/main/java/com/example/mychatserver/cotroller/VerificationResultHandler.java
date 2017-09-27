package com.example.mychatserver.cotroller;

import com.example.mychatserver.MyChatService;
import com.example.mychatserver.TypeConstant;
import com.example.mychatserver.bean.FriendBean;
import com.example.mychatserver.bean.NetBean;
import com.example.mychatserver.db.bean.FriendshipBean;
import com.example.mychatserver.db.bean.OfflineBean;
import com.example.mychatserver.db.bean.UserBean;
import com.example.mychatserver.db.dao.FriendshipDao;
import com.example.mychatserver.db.dao.OfflineDao;
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
 * Created by wzj on 2017/9/27.
 */

public class VerificationResultHandler implements RequestHandler {

    private String request;
    private Socket mSocket;

    public VerificationResultHandler(String request, Socket socket) {
        this.request = request;
        mSocket = socket;
    }

    @Override
    public void handleRequest() {
        OfflineBean data = getData();

        /**
         * 向发起方发送信息
         */
        BufferedWriter writer = MyChatService.onlineUser.get(data.getToUser());
        UserDao u_dao = new UserDao();
        FriendshipDao f_dao = new FriendshipDao();
        String json = null;

        if (data.getType() == 2) {
            //同意添加好友 更新friendship表 向用户发送好友信息
            //由于标表的结构不同 from 和 to反过来
            FriendshipBean friendshipBean = new FriendshipBean(data.getToUser(), data.getFromUser(), 1, "", "", data.getDate());
            f_dao.updateResult(friendshipBean);

            //向用户发送好友信息
            FriendshipBean friendshipBean1 = f_dao.checkExist(friendshipBean);
            UserBean userBean = u_dao.findWithPhone(data.getFromUser());
            FriendBean bean = new FriendBean(data.getFromUser(), userBean.getNick(), userBean.getArea(), friendshipBean1.getRemarkA(), userBean.getSex());
            NetBean<FriendBean> netBean = new NetBean<>(TypeConstant.OK_VERIFICATION, bean);
            json = new Gson().toJson(netBean, NetBean.class);
        } else if(data.getType() == 3){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("type", TypeConstant.REFUSE_VERIFICATION);
                jsonObject.put("data", u_dao.findWithPhone(data.getFromUser()).getNick());
                json = jsonObject.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(writer!=null){
            try {
                writer.write(json);
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            OfflineDao o_dao = new OfflineDao();
            data.setMessage(json);
            o_dao.addOffline(data);
        }


        /**
         * 向被申请者发送消息
         */

        if(data.getType() == 2){
            UserBean t_userBean = u_dao.findWithPhone(data.getToUser());
            FriendBean bean = new FriendBean(data.getToUser(), t_userBean.getNick(), t_userBean.getArea(), t_userBean.getNick(), t_userBean.getSex());
            NetBean<FriendBean> netBean = new NetBean<>(TypeConstant.OK_VERIFICATION, bean);
            json = new Gson().toJson(netBean, NetBean.class);
        }else if(data.getType() == 3){
            JSONObject jsonObject = new JSONObject();
            json = jsonObject.toString();
            try {
                jsonObject.put("data","ok");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            OutputStreamWriter opw = new OutputStreamWriter(mSocket.getOutputStream());
            BufferedWriter writer1 = new BufferedWriter(opw);
            writer1.write(json);
            writer1.newLine();
            writer1.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private OfflineBean getData() {
        Gson gson = new Gson();
        Type type = new TypeToken<NetBean<OfflineBean>>() {
        }.getType();
        NetBean<OfflineBean> netBean = gson.fromJson(request, type);
        OfflineBean data = netBean.getData();
        return data;
    }
}
