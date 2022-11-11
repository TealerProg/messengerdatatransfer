package com.tealer.process.client.service;

import com.tealer.process.client.MessageQueueWorkerThread;
import com.tealer.process.client.beat.HeartBeatTimer;

/**
 * 消息管理对象
 *
 * @author Created by lipengbo
 * @email 1162947801@qq.com
 * @time Created on 2022/11/10 19:51
 */
public class ClientMessageModel {

    /**
     * 包名
     */
    private String packageName;
    /**
     * 消息工作线程
     */
    private MessageQueueWorkerThread messageQueueWorkerThread=null;
    /**
     * 心跳计数对象
     */
    private HeartBeatTimer heartBeatTimer=null;

    /**
     *服务连接对象
     */
    private ClientMessageServiceConnection clientMessageServiceConnection=null;

    public ClientMessageModel(String packageName){
      this.packageName=packageName;
    }

    public String getPackageName() {
        return packageName;
    }

    public MessageQueueWorkerThread getMessageQueueWorkerThread() {
        return messageQueueWorkerThread;
    }

    public void setMessageQueueWorkerThread(MessageQueueWorkerThread messageQueueWorkerThread) {
        this.messageQueueWorkerThread = messageQueueWorkerThread;
    }


    public HeartBeatTimer getHeartBeatTimer() {
        return heartBeatTimer;
    }

    public void setHeartBeatTimer(HeartBeatTimer heartBeatTimer) {
        this.heartBeatTimer = heartBeatTimer;
    }

    public ClientMessageServiceConnection getClientMessageServiceConnection() {
        return clientMessageServiceConnection;
    }

    public void setClientMessageServiceConnection(ClientMessageServiceConnection clientMessageServiceConnection) {
        this.clientMessageServiceConnection = clientMessageServiceConnection;
    }
}
