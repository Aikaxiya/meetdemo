package com.hw.meetdemo.databind;

import androidx.databinding.ObservableField;

/**
 * @author: Andrew chen
 * @date: 2022/2/16 11:47
 * @description: 双向绑定数据
 */
public class RoomObserver {

    //会见roomCode
    public final ObservableField<String> meetRoomCode = new ObservableField<>();
    //会见编号
    public final ObservableField<String> meetNumber = new ObservableField<>();
    //目标编号
    public final ObservableField<String> targetNumber = new ObservableField<>();
    //是否隐藏静音按钮(麦克风)
    public final ObservableField<Boolean> hideMicButton = new ObservableField<>(false);
    //是否显示禁用麦克风
    public final ObservableField<Boolean> showDisableMicButton = new ObservableField<>(false);
}
