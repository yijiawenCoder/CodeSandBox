package com.yijiawencoder.codesandbox.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import com.yijiawencoder.codesandbox.model.codeSandBox.ExecuteCodeRequest;
import com.yijiawencoder.codesandbox.model.codeSandBox.ExecuteCodeResponse;
import com.yijiawencoder.codesandbox.service.CodeSandBox;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
            int exitValue = process.waitFor();
            if (exitValue == 0) {
                //正常退出
                System.out.println("编译成功");
                //分批获取获得控制台信息
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                //把输出拼成一串
                StringBuilder compileOutputBuilder = new StringBuilder();
                //逐行读取
                String compileOutputLine = bufferedReader.readLine();
                while ((compileOutputLine = bufferedReader.readLine()) != null) ;
                {
                    compileOutputBuilder.append(compileOutputLine);
                }
                System.out.println("编译输出：" + compileOutputBuilder.toString());

            } else {
                //异常退出
                System.out.println("编译失败，请检查代码");
                //分批获取获得控制台信息
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                //把输出拼成一串
                StringBuilder compileOutputBuilder = new StringBuilder();
                //逐行读取
                String compileOutputLine = bufferedReader.readLine();
                while ((compileOutputLine = bufferedReader.readLine()) != null) ;
                {
                    compileOutputBuilder.append(compileOutputLine);
                }
                System.out.println("编译输出：" + compileOutputBuilder.toString());
                //分批获取获得控制台信息
                //获取错误流

                BufferedReader errorBufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                StringBuilder errorCompileOutputBuilder = new StringBuilder();
                //逐行读取
                String errorCompileOutputLine = bufferedReader.readLine();
                while ((compileOutputLine = bufferedReader.readLine()) != null) ;
                {
                    errorCompileOutputBuilder.append("compileOutputLine");
                }

                System.out.println("编译输出：" + compileOutputBuilder.toString());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
