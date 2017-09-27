package com.example.mychatserver.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mychatserver.MyApplication;
import com.example.mychatserver.db.ServerDB;
import com.example.mychatserver.db.bean.UserBean;

/**
 * Created by wzj on 2017/9/23.
 */

public class UserDao {

    private static final String TABLENAME = ServerDB.USERTABLE;
    private  ServerDB dbHelper;

    public UserDao() {
        dbHelper = new ServerDB(MyApplication.getmContext(),"server");
    }

    public boolean addUser (UserBean bean){
        if (bean == null)
            return false;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ServerDB.UserColum.PHONE, bean.getPhone());
        values.put(ServerDB.UserColum.AREA, bean.getArea());
        values.put(ServerDB.UserColum.SEX, bean.getSex());
        values.put(ServerDB.UserColum.NICK, bean.getNick());
        values.put(ServerDB.UserColum.PWD, bean.getPwd());
        long result = db.insert(TABLENAME, null, values);
        db.close();
        return result != -1;
    }

    public UserBean findWithPhone(String phone){
        UserBean bean =null;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLENAME + " where phone = ?", new String[]{phone});
        while (cursor.moveToNext()){
            String ph = cursor.getString(1);
            String nick = cursor.getString(2);
            String area = cursor.getString(3);
            String pwd = cursor.getString(4);
            int sex = cursor.getInt(5);
            bean = new UserBean(ph,area,nick,pwd,sex);
        }
        return bean;
    }
}
