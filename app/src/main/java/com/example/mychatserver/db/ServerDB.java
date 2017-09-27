package com.example.mychatserver.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wzj on 2017/9/23.
 */

public class ServerDB extends SQLiteOpenHelper {


    //user表
    public static final  String USERTABLE = "User";
    private static final String CREATE_USER = "create table User (" +
            "id integer primary key autoincrement , " +
            "phone varchar, " +
            "nick varchar," +
            "area varchar," +
            "pwd varchar," +
            "sex integer)";

    public static class UserColum{
        public static final String PHONE = "phone";
        public static final String NICK = "nick";
        public static final String AREA = "area";
        public static final String PWD = "pwd";
        public static final String SEX = "sex";
    }


    //friendship表
    public static final  String FRIENDSHIPTABLE = "Friendship";
    public static class FriendshipColum{
        public static final String FROMUSER = "fromUser";
        public static final String TOUSER= "toUser";
        public static final String RESULT = "result";
        public static final String REMARK_A = "remarkA";
        public static final String REMARK_B = "remarkB";
        public static final String DATE = "date";
    }
    private static final String CREATE_FRIEND = "create table Friendship (" +
            "id integer primary key autoincrement , " +
            "fromUser varchar, " +
            "toUser varchar," +
            "result integer(1)," +
            "remarkA varchar," +
            "remarkB varchar," +
            "date integer)";

    //offline表
    public static final  String OFFLINEMESSAGE = "OfflineMessage";
    public static class OfflineColum{
        public static final String fromUser = "fromUser";
        public static final String TOUSER= "toUser";
        public static final String TYPE = "type";
        public static final String MESSAGE = "message";
        public static final String DATE = "date";
    }
    private static final String CREATE_OFFLINE = "create table "+OFFLINEMESSAGE+" (" +
            "id integer primary key autoincrement , " +
            "fromUser varchar, " +
            "toUser varchar," +
            "type integer(1)," +
            "message text," +
            "date integer)";

    public ServerDB(Context context, String name) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USER);
        sqLiteDatabase.execSQL(CREATE_FRIEND);
        sqLiteDatabase.execSQL(CREATE_OFFLINE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
