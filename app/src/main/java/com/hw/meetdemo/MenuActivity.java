package com.hw.meetdemo;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.hw.meetdemo.base.AppManager;
import com.hw.meetdemo.base.BaseActivity;
import com.hw.meetdemo.databind.RoomBean;
import com.hw.meetdemo.databind.RoomObserver;
import com.hw.meetdemo.databinding.MenuActivityBinding;
import com.hw.meetdemo.util.CameraUtil;

import java.util.HashSet;
import java.util.Set;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;

/**
 * @author: Andrew chen
 * @date: 2022/2/16 10:28
 * @description:
 */
public class MenuActivity extends BaseActivity {

    MenuActivityBinding menuActivityBinding;
    public RoomObserver roomObserver = new RoomObserver();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        menuActivityBinding = DataBindingUtil.setContentView(this, R.layout.menu_activity);
        menuActivityBinding.setRoomObserver(roomObserver);
        roomObserver.meetRoomCode.set("102");
        roomObserver.meetNumber.set(Build.MODEL);
        //呼叫按钮点击
        menuActivityBinding.meetCallBtn.setOnClickListener(v -> {
            String meetNum = roomObserver.meetNumber.get();
            String meetRoomCode = roomObserver.meetRoomCode.get();
            if (StrUtil.isBlank(meetRoomCode) || StrUtil.isBlank(meetNum)) {
                Toast.makeText(this, "请输入昵称", Toast.LENGTH_SHORT).show();
            } else {
                RoomBean.roomCode = meetRoomCode;
                RoomBean.FROM = meetNum;
                //跳转页面
                AppManager.getInstance().startActivity(RoomActivity.class);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
