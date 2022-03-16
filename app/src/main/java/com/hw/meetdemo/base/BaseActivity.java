package com.hw.meetdemo.base;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author: Andrew chen
 * @date: 2022/2/16 10:35
 * @description:
 */
public class BaseActivity extends AppCompatActivity {

    protected Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = this;
        // 尝试将activity设置为横屏
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        Log.i("Android OS:", android.os.Build.VERSION.RELEASE);
        //安卓设置沉浸状态栏
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.setStatusBarColor(Color.TRANSPARENT);
        ViewGroup rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.setPadding(0, 0, 0, 0);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        bindContentView();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        bindContentView();
    }

    //绑定View
    private void bindContentView() {
        //Activity压栈
        AppManager.getInstance().addActivity(activity);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AppManager.getInstance().finishActivity();
    }

}
