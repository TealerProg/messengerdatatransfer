package com.tealer.process.client.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Messenger;

import com.tealer.process.client.callback.ServiceCallback;
import com.tealer.process.client.callback.ServiceConnectCallback;
import com.tealer.process.client.log.LogHelper;

/**
 * 描述
 *
 * @author Created by lipengbo
 * @email 1162947801@qq.com
 * @time Created on 2022/11/10 19:53
 */
public class ClientMessageServiceConnection implements ServiceConnection {

    public static final String TAG = ClientMessageServiceConnection.class.getSimpleName();

    private Context context;

    /**
     * 消息工作线程
     */
    private ServiceCallback serviceCallback = null;

    /**
     * 通信对象
     */
    private Messenger serviceMessenger = null;

    public ClientMessageServiceConnection(Context context, ServiceCallback serviceCallback) {
        this.context = context;
        this.serviceCallback = serviceCallback;
    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        Messenger messenger = new Messenger(binder);
        //记录通信对象
        serviceMessenger = messenger;
        //Messenger轮询列表
        Messenger replyMessenger = new Messenger(new Handler(new ClientMessageHandlerCallback(context, name)));
        //设置非阻塞
        if (null != serviceCallback) {
            serviceCallback.onServcieConnected(messenger, replyMessenger);
        }
        LogHelper.i(TAG, "[onServiceConnected] 连接成功!");

    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        //服务断开
        onDisconnected();
        LogHelper.i(TAG, "[onServiceDisconnected] 连接断开!");
    }

    /**
     * 判断客户端是否连接
     *
     * @return
     */
    public boolean isConnected() {
        IBinder binder = (null != serviceMessenger) ? serviceMessenger.getBinder() : null;
        return binder != null ? binder.pingBinder() : false;
    }


    public void onDisconnected() {
        serviceMessenger = null;
        //服务断开
        if (null != serviceCallback) {
            serviceCallback.onServiceConnected();
        }
    }


}
