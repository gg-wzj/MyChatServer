package com.example.mychatserver.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mychatserver.MyApplication;
import com.example.mychatserver.db.ServerDB;
import com.example.mychatserver.db.bean.OfflineBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wzj on 2017/9/24.
 */

public class OfflineDao {

    private final ServerDB dbHelper;
    private static final String TABLENAME = ServerDB.OFFLINEMESSAGE;

    public OfflineDao() {
        dbHelper = new ServerDB(MyApplication.getmContext(),"server");
    }

    public boolean addOffline(OfflineBean bean){
        if (bean == null)
            return false;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ServerDB.OfflineColum.fromUser, bean.getFromUser());
        values.put(ServerDB.OfflineColum.TOUSER, bean.getToUser());
        values.put(ServerDB.OfflineColum.TYPE, bean.getType());
        values.put(ServerDB.OfflineColum.MESSAGE, bean.getMessage());
        values.put(ServerDB.OfflineColum.DATE, bean.getDate());
        long result = db.insert(TABLENAME, null, values);
        db.close();
        return result != -1;
    }


    public List<OfflineBean> findWithToUser(String toUser){
        /*select  * from Friendship where (fromUser = "12345" or toUser = "12345") and result = 1 */
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<OfflineBean> list  = new ArrayList<>();
        OfflineBean bean ;
        Cursor cursor = db.rawQuery("select * from " + TABLENAME + " where toUser = ?", new String[]{toUser});
        while (cursor.moveToNext()){
            String fromUser = cursor.getString(1);
            String toUser1 = cursor.getString(2);
            int tpye = cursor.getInt(3);
            String message = cursor.getString(4);
            long date = cursor.getLong(5);
            bean = new OfflineBean(fromUser,toUser1,tpye,message,date);
            list.add(bean);
        }
        cursor.close();
        db.close();
        return list;
    }

    public void deleteWithToUser(String toUser){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLENAME, "toUser = ?", new String[]{toUser});
        db.close();
    }
}
