package com.yijiawencoder.codesandbox.model;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * 代码沙箱中的运行时间，按需加入
 */
public class ExecuteCodeResponse {
    /**
     * 输出一组结果
     */
    private List<String> outList;
    /**
     * 接口信息
     */
    private String message;
    /**
     * 执行状态
     */
    private  Integer status;
    /**
     * 接受代码
     */
    private String Code;
    /**
     * 判题信息
     */
    private JudgeInfo judgeInfo;
}
