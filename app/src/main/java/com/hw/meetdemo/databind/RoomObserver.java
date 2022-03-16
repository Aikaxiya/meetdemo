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
    //是否静音
    public final ObservableField<Boolean> muteMic = new ObservableField<>(false);
    //是否静音麦克风
    public final ObservableField<Boolean> disableMic = new ObservableField<>(false);
    //是否打开摄像头
    public final ObservableField<Boolean> openCamera = new ObservableField<>(false);
    public final ObservableField<Boolean> enableShare = new ObservableField<>(false);
}
