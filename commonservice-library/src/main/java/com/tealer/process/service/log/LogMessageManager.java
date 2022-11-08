package com.tealer.process.service.log;

/**
 * 日志消息管理类
 *
 * @author Created by lipengbo
 * @email 1162947801@qq.com
 * @time Created on 2022/11/8 21:11
 */
public class LogMessageManager {

    /**
     * 日志工作线程
     */
    private static LogMessageWorkerThread logMessageWorkerThread = new LogMessageWorkerThread();

    /**
     * 启动日志服务
     */
    public static void start() {
        //启动日志服务
        logMessageWorkerThread.startService();
    }

    /**
     * 停止服务
     */
    public static void stop(){
       logMessageWorkerThread.stopService();
    }

    /**
     * 发送一个消息
     * @param message
     */
    public static void post(String message){
       logMessageWorkerThread.postMessage(message);
    }

    /**
     * 设置消息输入监听
     * @param listener
     */
    public static void addOnMessageListener(LogMessageWorkerThread.OnMessageListener listener){
        logMessageWorkerThread.addOnMessageListener(listener);
    }

    /**
     * 设置消息输出监听
     * @param listener
     */
    public static void removeOnMessageListener(LogMessageWorkerThread.OnMessageListener listener){
        logMessageWorkerThread.removeOnMessageListner(listener);
    }


}
