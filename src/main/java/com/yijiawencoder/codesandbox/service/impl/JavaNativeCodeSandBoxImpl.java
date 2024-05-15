package com.yijiawencoder.codesandbox.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import com.yijiawencoder.codesandbox.model.ExecuteCodeRequest;
import com.yijiawencoder.codesandbox.model.ExecuteCodeResponse;
import com.yijiawencoder.codesandbox.model.ExecuteMessage;
import com.yijiawencoder.codesandbox.model.JudgeInfo;
import com.yijiawencoder.codesandbox.service.CodeSandBox;
import com.yijiawencoder.codesandbox.utils.ProcessUtils;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


public class JavaNativeCodeSandBoxImpl implements CodeSandBox {
    public static final String GLOBAL_CODE_DIR_NAME = "tmpCode";
    public static final String GLOBAL_JAVA_CLASS_NAME = "Main.java";

    public static void main(String[] args) {
        JavaNativeCodeSandBoxImpl javaNativeCodeSandBox = new JavaNativeCodeSandBoxImpl();
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setInputList(Arrays.asList("1 2", "1 3"));
        executeCodeRequest.setLanguage("java");
        executeCodeRequest.setCode(ResourceUtil.readStr("testCode/computeArgs/SimpleCompute.java", StandardCharsets.UTF_8));
        ExecuteCodeResponse executeCodeResponse = javaNativeCodeSandBox.executeCode(executeCodeRequest);
        System.out.println(executeCodeResponse);
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();
        String language = executeCodeRequest.getLanguage();
        String userDir = System.getProperty("user.dir");
        //兼容不同系统
        String globalCodePathName = userDir + File.separator + GLOBAL_CODE_DIR_NAME;
        //判断全局代码目录是否存在
        if (FileUtil.exist(globalCodePathName)) {
            //没有则新建
            FileUtil.mkdir(globalCodePathName);
        }
        //把用户的代码隔级存放
        String userCodeParentPath = globalCodePathName + File.separator + UUID.randomUUID();
        String userCodePath = userCodeParentPath + File.separator + GLOBAL_JAVA_CLASS_NAME;
        File userCodeFile = FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);
        //编译代码得到class文件
        String compileCmd = String.format(" javac -encoding utf-8  %s", userCodeFile.getAbsolutePath());
        //得到一个进程
        try {
            Process process = Runtime.getRuntime().exec(compileCmd);
            //等待进程执行，返回退出码
            ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(process, "编译");
            System.out.println(executeMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //执行代码
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        for (String inputArgs : inputList) {
            StopWatch stopWatch = new StopWatch();
            String runCmd = String.format("java -Dfile.encoding=UTF-8 -cp %s Main %s", userCodeParentPath, inputArgs);
            try {
                Process runProcess = Runtime.getRuntime().exec(runCmd);
                ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(runProcess, "运行");
                System.out.println(executeMessage);
                executeMessageList.add(executeMessage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        //整理输出
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        List<String>outPutList = new ArrayList<>();
        //取用时最大值
        long maxExecuteTime = 0;
        for (ExecuteMessage executeMessage : executeMessageList) {
            if(StrUtil.isNotBlank(executeMessage.getErrorMessage())){
                String  errorMessage = executeMessage.getErrorMessage();
                executeCodeResponse.setMessage(errorMessage);
                //执行中存在错误
                executeCodeResponse.setStatus(3);
                break;
            }
            Long time = executeMessage.getTime();
            if(time!=null ){
                maxExecuteTime = Math.max(maxExecuteTime,time);
            }
            outPutList.add(executeMessage.getMessage());
            executeCodeResponse.setMessage(executeMessage.getMessage());

        }
        //执行到最后还没有报错
        executeCodeResponse.setOutList(outPutList);
        if(outPutList.size()==executeMessageList.size()){
            executeCodeResponse.setStatus(1);
        }

        JudgeInfo judgeInfo = new JudgeInfo();
        //todo 查看内存
       // judgeInfo.setMemory();
        judgeInfo.setTime(maxExecuteTime);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        //文件清理
     if(userCodeFile.getParentFile()!=null) {
         boolean del = FileUtil.del(userCodeFile.getParentFile());

     }

     //错误处理



        return executeCodeResponse;
    }
}
