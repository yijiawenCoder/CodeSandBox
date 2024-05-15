package com.yijiawencoder.codesandbox.model;

import lombok.Data;

@Data
public class JudgeInfo {
    /**
     *程序执行信息
     */
    private String message;
    /**
     * 执行消耗的内存
     */
    private long memory;
    /**
    消耗时间
     */
    private long time;
}
