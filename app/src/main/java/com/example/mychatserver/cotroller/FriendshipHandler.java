package com.example.mychatserver.cotroller;

import com.example.mychatserver.TypeConstant;
import com.example.mychatserver.bean.FriendBean;
import com.example.mychatserver.bean.NetBean;
import com.example.mychatserver.db.bean.FriendshipBean;
import com.example.mychatserver.db.bean.UserBean;
import com.example.mychatserver.db.dao.FriendshipDao;
import com.example.mychatserver.db.dao.UserDao;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wzj on 2017/9/30.
 */

public class FriendshipHandler implements RequestHandler {

    private String request;
    private Socket mSocket;

    public FriendshipHandler(String request, Socket socket) {
        this.request = request;
        mSocket = socket;
    }

    @Override
    public void handleRequest() {
        try {
            JSONObject jsonObject = new JSONObject(request);
            String data = jsonObject.optString("data");

            FriendshipDao dao = new FriendshipDao();
            UserDao u_dao = new UserDao();
            List<FriendshipBean> list = dao.findWithPhone(data);

            List<FriendBean> resultList = new ArrayList<>();
            for (FriendshipBean friendshipBean : list) {
                String phone;
                String remark;
                if (friendshipBean.getFromUser().equals(data)) {
                    //好友发起人请求列表
                    phone = friendshipBean.getToUser();
                    remark  = friendshipBean.getRemarkA();
                } else {
                    //好友接受方请求列表
                    phone = friendshipBean.getFromUser();
                     remark = friendshipBean.getRemarkB();
                }

                UserBean userBean = u_dao.findWithPhone(phone);

                FriendBean bean = new FriendBean(phone, userBean.getNick(),userBean.getArea(),remark,userBean.getSex() );
                resultList.add(bean);
            }
            NetBean<List<FriendBean>> netBean = new NetBean<>(TypeConstant.RESPOND_FRIENDSHIP,resultList);
            String json = new Gson().toJson(netBean, NetBean.class);

            OutputStreamWriter opw = new OutputStreamWriter(mSocket.getOutputStream());
            BufferedWriter writer = new BufferedWriter(opw);
            writer.write(json);
            writer.newLine();
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
