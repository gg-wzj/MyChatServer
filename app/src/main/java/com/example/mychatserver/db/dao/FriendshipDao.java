package com.example.mychatserver.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mychatserver.MyApplication;
import com.example.mychatserver.db.ServerDB;
import com.example.mychatserver.db.bean.FriendshipBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wzj on 2017/9/24.
 */

public class FriendshipDao {

    private static final String TABLENAME = ServerDB.FRIENDSHIPTABLE;
    private  ServerDB dbHelper;

    public FriendshipDao() {
        dbHelper = new ServerDB(MyApplication.getmContext(),"server");
    }

    public boolean addFriendship(FriendshipBean bean){
        if (bean == null)
            return false;
        //有重复的先删除
        if(checkExist(bean)!=null)
            delete(checkExist(bean));
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ServerDB.FriendshipColum.FROMUSER,bean.getFromUser() );
        values.put(ServerDB.FriendshipColum.TOUSER,bean.getToUser() );
        values.put(ServerDB.FriendshipColum.RESULT,bean.getResult() );
        values.put(ServerDB.FriendshipColum.REMARK_A,bean.getRemarkA() );
        values.put(ServerDB.FriendshipColum.REMARK_B,bean.getRemarkB() );
        values.put(ServerDB.FriendshipColum.DATE,bean.getDate() );

        long result = db.insert(TABLENAME, null, values);
        db.close();
        return result != -1;
    }

    public List<FriendshipBean> findWithPhone(String phone){
        List<FriendshipBean> list = new ArrayList<>();
        FriendshipBean bean;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        /*select  * from Friendship where (fromUser = "12345" or toUser = "12345") and result = 1 */
        Cursor cursor = db.rawQuery("select  * from " + TABLENAME + " where (fromUser = ? or toUser = ?) and result = 1",
                new String[]{phone, phone});
        while (cursor.moveToNext()){
            String fromUser = cursor.getString(1);
            String toUser = cursor.getString(2);
            int result = cursor.getInt(3);
            String remarkA = cursor.getString(4);
            String remarkB = cursor.getString(5);
            long date = cursor.getLong(6);
            bean = new FriendshipBean(fromUser,toUser,result,remarkA,remarkB,date);
            list.add(bean);
        }

        return list;
    }

    public FriendshipBean checkExist(FriendshipBean bean){
        if(bean == null)
            return null;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLENAME + " where fromUser = ? and toUser = ?", new String[]{bean.getFromUser(), bean.getToUser()});
        while (cursor.moveToNext()){
            String fromUser = cursor.getString(1);
            String toUser = cursor.getString(2);
            int result = cursor.getInt(3);
            String remarkA = cursor.getString(4);
            String remarkB = cursor.getString(5);
            long date = cursor.getLong(6);
            cursor.close();
            db.close();
            return new FriendshipBean(fromUser,toUser,result,remarkA,remarkB,date);
        }
        cursor.close();
        db.close();
        return null;
    }

    public void delete(FriendshipBean bean){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLENAME, "fromUser = ? and toUser = ?", new String[]{bean.getFromUser(), bean.getToUser()});
        db.close();
    }


    public void updateResult(FriendshipBean bean){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("result",1);
        db.update(TABLENAME,values,"fromUser = ? and toUser = ? ",new String[]{bean.getFromUser(),bean.getToUser()} );
        db.close();
    }


}
