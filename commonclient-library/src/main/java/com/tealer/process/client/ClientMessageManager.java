package com.tealer.process.client;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.tealer.process.client.beat.HeartBeatTimer;
import com.tealer.process.client.log.LogDelegate;
import com.tealer.process.client.log.LogHelper;
import com.tealer.process.client.service.ClientMessageModel;
import com.tealer.process.client.service.ClientMessageSupportComponent;

/**
 * 服务端通信对象，用于管理所有Model对象,并处理消息
 *
 * @author Created by lipengbo
 * @email 1162947801@qq.com
 * @time Created on 2022/11/10 19:43
 */
public class ClientMessageManager {

    private ClientMessageSupportComponent clientMessageSupportComponent = null;

    private final static ClientMessageManager clientMessageModel = new ClientMessageManager();

    private ClientMessageManager() {
    }

    public static ClientMessageManager get() {
        return clientMessageModel;
    }

    /**
     * 进行通信服务的环境初始化操作
     *
     * @param context
     * @return
     */
    public ClientMessageManager init(Context context) {
        clientMessageSupportComponent = new ClientMessageSupportComponent(context);
        return this;
    }

    /**
     * Log开关。建议测试环境开启，线上环境应该关闭。
     *
     * @param isEnable
     */
    public ClientMessageManager setEnableLog(boolean isEnable) {
        LogHelper.setLogLevel(isEnable ? Log.DEBUG : Log.ASSERT);
        return this;
    }

    /**
     * Log扩展接口，方便做日志输出定制（不设置，默认使用DefaultLogDelegate）
     *
     * @param delegate
     */
    public ClientMessageManager setLogDelegate(@NonNull LogDelegate delegate) {
        LogHelper.setLogDelegate(delegate);
        return this;
    }


    /**
     * 绑定通信服务
     *
     * @param packageName
     * @return
     */
    public boolean bindService(String packageName) {
        if (null == clientMessageSupportComponent) {
            throw new IllegalArgumentException("请在Application#onCreate调用init(Context context)");
        }
        return clientMessageSupportComponent.bindService(packageName);
    }


    /**
     * 开启心跳探测机制
     *
     * @param packageName
     * @param heartBeatTimer
     */
    public void startHeartBeatTimer(String packageName, HeartBeatTimer heartBeatTimer) {
        if (null == clientMessageSupportComponent) {
            throw new IllegalArgumentException("请在Application#onCreate调用init(Context context)");
        }
        clientMessageSupportComponent.startHeartBeatTimer(packageName, heartBeatTimer);
    }

    /**
     * 客户端是否连接
     *
     * @param packageName
     * @return
     */
    public boolean isConnected(String packageName) {
        if (null == clientMessageSupportComponent) {
            throw new IllegalArgumentException("请在Application#onCreate调用init(Context context)");
        }
        return clientMessageSupportComponent.isConnected(packageName);
    }

    /**
     * 解除服务绑定
     *
     * @param packageName
     */
    public void onServiceDisconnected(String packageName) {
        if (null == clientMessageSupportComponent) {
            throw new IllegalArgumentException("请在Application#onCreate调用init(Context context)");
        }
        clientMessageSupportComponent.onServiceDisconnected(packageName);
    }


    /**
     * 停止服务
     *
     * @param packageName
     */
    public void unbindServie(String packageName) {
        if (null == clientMessageSupportComponent) {
            throw new IllegalArgumentException("请在Application#onCreate调用init(Context context)");
        }
        clientMessageSupportComponent.unbindService(packageName);
    }

    /**
     * 添加一条日志消息
     * @param packageName
     * @param message
     */
    public void addLogMessage(String packageName, String message) {
        if (null == clientMessageSupportComponent) {
            throw new IllegalArgumentException("请在Application#onCreate调用init(Context context)");
        }
        clientMessageSupportComponent.addLogMessage(packageName, message);
    }

    /**
     * 添加请求消息
     * @param packageName
     * @param message
     */
    public void addMessage(String packageName, Message message){
        if (null == clientMessageSupportComponent) {
            throw new IllegalArgumentException("请在Application#onCreate调用init(Context context)");
        }
        clientMessageSupportComponent.addMessage(packageName,message);
    }

}
