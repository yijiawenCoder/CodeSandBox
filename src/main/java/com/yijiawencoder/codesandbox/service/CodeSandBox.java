package com.yijiawencoder.codesandbox.service;


import com.yijiawencoder.codesandbox.model.ExecuteCodeRequest;
import com.yijiawencoder.codesandbox.model.ExecuteCodeResponse;

/**
 * 代码沙箱接口定义
 * @author yijiawenCoder
 */
public interface CodeSandBox {
    /**
     *  执行代码
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
