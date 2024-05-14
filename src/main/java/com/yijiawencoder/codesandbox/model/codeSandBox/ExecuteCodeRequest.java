package com.yijiawencoder.codesandbox.model.codeSandBox;

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
public class ExecuteCodeRequest {
    /**
     * 接受一组用例
     */
    private List<String> inputList;
    /**
     * 接受编程语言
     */
    private  String Language;
    /**
     * 接受代码
     */
    private String Code;
}
