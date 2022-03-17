package com.hw.meetdemo.databind;

import com.hw.mediasoup.vm.PeerProps;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: 13105
 * @date: 2022/3/17 11:00
 * @description:
 */
@Data
@AllArgsConstructor
public class MemberProps {

    private boolean needRender;
    private PeerProps peerProps;

}
