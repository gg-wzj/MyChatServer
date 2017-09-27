package com.example.mychatserver.bean;

/**
 * Created by wzj on 2017/9/27.
 */

public class FriendBean {

    String phone;
    String nick;
    String area;
    String remark;
    int sex;

    public FriendBean(String phone, String nick, String area, String remark, int sex) {
        this.phone = phone;
        this.nick = nick;
        this.area = area;
        this.remark = remark;
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
