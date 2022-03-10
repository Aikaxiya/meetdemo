package com.hw.meetdemo;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.hw.meetdemo.base.AppManager;
import com.hw.meetdemo.base.BaseActivity;
import com.hw.meetdemo.databind.RoomBean;
import com.hw.meetdemo.databind.RoomObserver;
import com.hw.meetdemo.databinding.MenuActivityBinding;

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
        //TODO 测试赋值
        roomObserver.meetRoomCode.set("102");
        roomObserver.meetNumber.set("windows");
        roomObserver.targetNumber.set("onePlus");
        //呼叫按钮点击
        menuActivityBinding.meetCallBtn.setOnClickListener(v -> {
            String meetNum = roomObserver.meetNumber.get();
            String targetNumber = roomObserver.targetNumber.get();
            String meetRoomCode = roomObserver.meetRoomCode.get();
            if (StrUtil.isBlank(meetRoomCode) || StrUtil.isBlank(meetNum) || StrUtil.isBlank(targetNumber)) {
                Toast.makeText(this, "请输入编号", Toast.LENGTH_SHORT).show();
            } else {
                RoomBean.roomCode = meetRoomCode;
                RoomBean.FROM = meetNum;
                RoomBean.TO = targetNumber;
                //跳转页面
                AppManager.getInstance().startActivity(RoomActivity.class);
            }
        });
    }
}
