package com.example.mychatserver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.mychatserver.bean.LoginBean;
import com.example.mychatserver.bean.NetBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void openServer(View v){
        Intent intent = new Intent(this,MyChatService.class);
        startService(intent);
    }

    public void closeServer(View v){
        Intent intent = new Intent(this,MyChatService.class);
        stopService(intent);
    }
}
