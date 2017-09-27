package com.example.mychatserver.db.bean;

/**
 * Created by wzj on 2017/9/24.
 */

public class FriendshipBean {
    String fromUser;
    String toUser;
    int result;
    String remarkA;
    String remarkB;
    long date;

    public FriendshipBean(String fromUser, String toUser, int result, String remarkA, String remarkB, long date) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.result = result;
        this.remarkA = remarkA;
        this.remarkB = remarkB;
        this.date = date;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getRemarkA() {
        return remarkA;
    }

    public void setRemarkA(String remarkA) {
        this.remarkA = remarkA;
    }

    public String getRemarkB() {
        return remarkB;
    }

    public void setRemarkB(String remarkB) {
        this.remarkB = remarkB;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
