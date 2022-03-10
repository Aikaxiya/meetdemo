package com.hw.meetdemo.databind;

import androidx.databinding.ObservableField;

/**
 * @author: Andrew chen
 * @date: 2022/2/14 22:31
 * @description:
 */
public class User2 {
    //mvvm双向数据
    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<String> age = new ObservableField<>();
}
