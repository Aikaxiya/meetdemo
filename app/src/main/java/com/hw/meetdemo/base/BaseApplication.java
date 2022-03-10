package com.hw.meetdemo.base;

import android.util.Log;

import androidx.multidex.MultiDexApplication;

import org.mediasoup.droid.Logger;
import org.mediasoup.droid.MediasoupClient;


/**
 * @author: Andrew chen
 * @date: 2022/2/14 16:44
 * @description:
 */
public class BaseApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler((thread, ex) -> {
            ex.printStackTrace();
            Log.i("错误日志", ex.getMessage());
        });
        //全局异常
        Logger.setLogLevel(Logger.LogLevel.LOG_DEBUG);
        Logger.setDefaultHandler();
        MediasoupClient.initialize(getApplicationContext());
        Log.d("MediaSoupClient", "MediaSoupClient 完成初始化");
    }
}
