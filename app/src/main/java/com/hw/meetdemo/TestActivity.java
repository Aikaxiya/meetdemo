package com.hw.meetdemo;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.hw.meetdemo.databind.User2;
import com.hw.meetdemo.databinding.MainActivityBinding;

/**
 * @author: Andrew chen
 * @date: 2022/2/14 19:58
 * @description:
 */
public class TestActivity extends AppCompatActivity {

    MainActivityBinding binding;
    User2 user2 = new User2();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity);
        //设置值
        binding.btnBind2.setOnClickListener(view -> {
            user2.name.set("userName1..");
            user2.age.set("userAge1..");
            binding.setUser2(user2);
        });
        //获取值
        binding.btnBind3.setOnClickListener(view -> {
            System.out.println("binding.getUser2().name.get()=" + binding.getUser2().name.get());
            System.out.println("user2.name.get()=" + user2.name.get());
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
