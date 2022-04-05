package com.hw.meetdemo.ui.dialog;

import android.content.Context;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

/**
 * @author: Andrew chen
 * @date: 2021/10/15 9:43
 * @description: dialog基础类
 */
public class DialogChild extends android.app.Dialog {

    private final Context context;

    public DialogChild(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        //设置软键盘点击非输入框区域关闭软键盘
        //ScreenUtils.getInstance().autoKeyboardSetting(context, getCurrentFocus(), event);
        return super.onTouchEvent(event);
    }

}
