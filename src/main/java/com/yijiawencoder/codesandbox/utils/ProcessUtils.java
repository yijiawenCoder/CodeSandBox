package com.yijiawencoder.codesandbox.utils;

import com.yijiawencoder.codesandbox.model.ExecuteMessage;
import org.springframework.util.StopWatch;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/***
 * 进程工具类
 */
public class ProcessUtils {
    /**
     * @param process
     * @param opName
     * @return
     */
    public static ExecuteMessage runProcessAndGetMessage(Process process, String opName) {
        ExecuteMessage executeMessage = new ExecuteMessage();


        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            int exitValue = process.waitFor();
            executeMessage.setExitValue(exitValue);

            if (exitValue == 0) {
                //正常退出

                System.out.println(opName + "成功");
                //分批获取获得控制台信息
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                //把输出拼成一串
                StringBuilder compileOutputBuilder = new StringBuilder();
                //逐行读取
                String compileOutputLine;
                while ((compileOutputLine = bufferedReader.readLine()) != null) {
                    compileOutputBuilder.append(compileOutputLine);
                }
                executeMessage.setMessage(compileOutputBuilder.toString());

            } else {
                //异常退出
                System.out.println(opName + "失败，请检查代码");
                //分批获取获得控制台信息
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                //把输出拼成一串
                StringBuilder compileOutputBuilder = new StringBuilder();
                //逐行读取
                String compileOutputLine;
                while ((compileOutputLine = bufferedReader.readLine()) != null) ;
                {
                    compileOutputBuilder.append(compileOutputLine);
                }
                executeMessage.setMessage(compileOutputBuilder.toString());
                //分批获取获得控制台信息
                //获取错误流

                BufferedReader errorBufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                StringBuilder errorCompileOutputBuilder = new StringBuilder();
                //逐行读取
                String errorCompileOutputLine;
                while ((errorCompileOutputLine = errorBufferedReader.readLine()) != null) ;
                {
                    errorCompileOutputBuilder.append(errorCompileOutputLine);
                }
                executeMessage.setErrorMessage(errorCompileOutputBuilder.toString());

            }
            stopWatch.stop();
            executeMessage.setTime(stopWatch.getLastTaskTimeMillis());
        } catch (Exception e) {

        }
        return executeMessage;
    }
}
