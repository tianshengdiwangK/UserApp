package com.android.userapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.android.userapp.kits.AppUsageUtil;

import java.util.Calendar;

public class AppDbPrepareService extends Service {
    private final int DELAY_LENGHT = 10000; // 10s刷新一次数据库
    private boolean stopflag=false;
    private static final String TAG = "AppUsageService";
    public AppDbPrepareService() {
    }

    //创建时调用
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    //服务销毁时调用
    @Override
    public void onDestroy() {
        stopflag=true;
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    //每次服务启动时调用
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStart");
        Log.d(TAG, "start Refresh database!");
        //权限检查
        AppUsageUtil.checkUsageStateAccessPermission(this);

        Calendar beginCal = Calendar.getInstance();
        //准备过去一天的应用数据
        beginCal.add(Calendar.HOUR_OF_DAY, -1);
        Calendar endCal = Calendar.getInstance();
        long start_time = beginCal.getTimeInMillis();
        long end_time = endCal.getTimeInMillis();
        AppUsageUtil.getAppUsageInfo(getApplicationContext(),start_time,end_time);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}