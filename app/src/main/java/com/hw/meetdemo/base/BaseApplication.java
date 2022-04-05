package com.hw.meetdemo.base;

import android.annotation.SuppressLint;
import android.content.Context;
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

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Thread.setDefaultUncaughtExceptionHandler((thread, ex) -> {
            ex.printStackTrace();
            StackTraceElement stackTraceElement = ex.getStackTrace()[0];
            String className = stackTraceElement.getClassName();
            String methodName = stackTraceElement.getMethodName();
            int lineNumber = stackTraceElement.getLineNumber();
            Log.i("错误日志", ex.getMessage() + "\n" + className + "\n" + methodName + "\n" + lineNumber);
        });
        //全局异常
        Logger.setLogLevel(Logger.LogLevel.LOG_DEBUG);
        Logger.setDefaultHandler();
        MediasoupClient.initialize(getApplicationContext());
        Log.d("MediaSoupClient", "MediaSoupClient 完成初始化");
    }

    public static Context getContext() {
        return context;
    }
}
