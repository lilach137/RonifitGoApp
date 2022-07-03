package com.example.ronifitgo.ronifitgo;


import android.app.Application;

import com.example.ronifitgo.ronifitgo.utils.DataManager;
import com.example.ronifitgo.ronifitgo.utils.MyDB;


public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //Initiate FireBase Managers
        DataManager.initHelper();
        MyDB.initHelper();
    }
}