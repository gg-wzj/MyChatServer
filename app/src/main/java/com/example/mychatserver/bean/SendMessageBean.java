package com.example.mychatserver.bean;

/**
 * Created by wzj on 2017/9/25.
 */

public class SendMessageBean {

    String phone;
    int  type;
    String message;
    int hasSend;
    long time;

    public SendMessageBean(String phone, int type, String message, int hasSend, long time) {
        this.phone = phone;
        this.type = type;
        this.message = message;
        this.hasSend = hasSend;
        this.time = time;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getHasSend() {
        return hasSend;
    }

    public void setHasSend(int hasSend) {
        this.hasSend = hasSend;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
