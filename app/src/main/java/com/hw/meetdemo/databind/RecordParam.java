package com.hw.meetdemo.databind;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Andrew chen
 * @date: 2022/2/16 17:42
 * @description: 录像参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecordParam {

    private List<String> peerIds;
    private String filePath;

}
