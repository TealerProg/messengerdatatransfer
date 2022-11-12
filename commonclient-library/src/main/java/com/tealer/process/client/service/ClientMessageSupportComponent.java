package com.tealer.process.client.service;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.os.Messenger;
import android.util.ArrayMap;

import androidx.annotation.NonNull;

import com.tealer.process.client.MessageQueueWorkerThread;
import com.tealer.process.client.beat.HeartBeatTimer;
import com.tealer.process.client.callback.ServiceCallback;
import com.tealer.process.client.config.ClientConstantConfig;
import com.tealer.process.client.log.LogHelper;
import com.tealer.process.client.utils.ServiceHelperCompat;

/**
 * 客户端消息管理组件
 *
 * @author Created by lipengbo
 * @email 1162947801@qq.com
 * @time Created on 2022/11/10 19:45
 */
public class ClientMessageSupportComponent {
    private static final String TAG = ClientMessageSupportComponent.class.getSimpleName();

    private final Context context;

    private final ArrayMap<String, ClientMessageModel> clients = new ArrayMap<>();

    public ClientMessageSupportComponent(Context context) {
        this.context = context;
    }

    public ClientMessageModel getSupportMessage(final String packageName) {
        ClientMessageModel supportMessage = clients.get(packageName);
        //create new if need.
        if (null == supportMessage) {
            supportMessage = new ClientMessageModel(packageName);
            final MessageQueueWorkerThread messageQueueWorkerThread = new MessageQueueWorkerThread();
            supportMessage.setMessageQueueWorkerThread(messageQueueWorkerThread);
            supportMessage.setClientMessageServiceConnection(new ClientMessageServiceConnection(context, new ServiceCallback() {
                @Override
                public void onServcieConnected(Messenger serviceMessenger, Messenger replyMessenger) {
                    messageQueueWorkerThread.setMessenger(serviceMessenger, replyMessenger);
                    addLogMessage(packageName, "ClientMessageManager[onServiceConnected] 连接成功!");
                }

                @Override
                public void onServiceConnected() {
                    //设置阻塞消息队列
                    messageQueueWorkerThread.setMessenger(null, null);
                    //发送消息
                    addLogMessage(packageName, "ClientMessageManager[onServiceDisconnected] 断开连接!");
                }
            }));
            clients.put(packageName, supportMessage);
        }
        return supportMessage;

    }

    public synchronized void removeSupportMessage(String packageName) {
        if (null == packageName) {
            return;
        }
        ClientMessageModel supportMessage = clients.get(packageName);
        if (null != supportMessage) {
            supportMessage.getClientMessageServiceConnection().onDisconnected();
            supportMessage.getHeartBeatTimer().cancel();
            //停止线程
            supportMessage.getMessageQueueWorkerThread().onStop();
            clients.remove(packageName);
        }
    }

    public boolean bindService(@NonNull String packageName) {
        ClientMessageModel supportMessage = getSupportMessage(packageName);
        //获取消息队列，对应的工作线程
        MessageQueueWorkerThread messageQueueWorkerThread = supportMessage.getMessageQueueWorkerThread();
        //启动工作线程
        if (!messageQueueWorkerThread.isAlive()) {
            messageQueueWorkerThread.start();
        }
        boolean bindService = false;
        //启动服务
        Intent intent = new Intent();
        intent.setAction(ServiceHelperCompat.getServiceAction(packageName, ClientConstantConfig.Common.KEY_SERVICE_ACTION_SUFFIX));
        Intent explicitFromImplicitIntent = ServiceHelperCompat.createExplicitFromImplicitIntent(context, intent);
        if (null != explicitFromImplicitIntent) {
            final Intent eIntent = new Intent(explicitFromImplicitIntent);
            bindService = context.bindService(eIntent, supportMessage.getClientMessageServiceConnection(), Context.BIND_AUTO_CREATE);
        }
        LogHelper.i(TAG, "Service Action：" + ServiceHelperCompat.getServiceAction(packageName, ClientConstantConfig.Common.KEY_SERVICE_ACTION_SUFFIX) + " startService bind is:" + bindService);
        if (!bindService) {
            LogHelper.w(TAG, "服务连接失败:" + intent.getAction() + " 请检测!");
        }
        return bindService;
    }


    /**
     * 启动心跳检测机制
     *
     * @param packageName
     * @param heartBeatTimer
     */
    public void startHeartBeatTimer(String packageName, HeartBeatTimer heartBeatTimer) {
        //启动心跳探测机制是否存活
        if (null != heartBeatTimer) {
            final ClientMessageModel supportMessage = getSupportMessage(packageName);
            //关闭上一个心跳计数器
            if (null != supportMessage.getHeartBeatTimer()) {
                supportMessage.getHeartBeatTimer().cancel();
            }
            supportMessage.setHeartBeatTimer(heartBeatTimer);
            supportMessage.getHeartBeatTimer().start();
        }
    }

    /**
     * 停止服务
     *
     * @param packageName
     */
    public void unbindService(String packageName) {
        ClientMessageModel supportMessage = getSupportMessage(packageName);
        ClientMessageServiceConnection launcherSupportServiceConnection = supportMessage.getClientMessageServiceConnection();
        if (isConnected(packageName)) {
            LogHelper.i(TAG, "unbindService");
            context.unbindService(launcherSupportServiceConnection);
        }
        removeSupportMessage(packageName);
    }

    /**
     * 客户端是否连接
     *
     * @param packageName
     * @return
     */
    public boolean isConnected(String packageName) {
        ClientMessageModel supportMessage = getSupportMessage(packageName);
        ClientMessageServiceConnection launcherSupportServiceConnection = supportMessage.getClientMessageServiceConnection();
        return launcherSupportServiceConnection.isConnected();
    }

    /**
     * 解除服务绑定
     *
     * @param packageName
     */
    public void onServiceDisconnected(String packageName) {
        ClientMessageModel supportMessage = getSupportMessage(packageName);
        boolean connected = isConnected(packageName);
        if (!connected) {
            supportMessage.getClientMessageServiceConnection().onDisconnected();
        }
    }


    /**
     * 添加一条日志消息
     *
     * @param packageName
     * @param message
     */
    public void addLogMessage(String packageName, String message) {
        //添加消息
        Message newMessage = Message.obtain();
        newMessage.what = ClientConstantConfig.Common.LOG_MESSAGE;
        newMessage.getData().putString(ClientConstantConfig.Common.KEY_APP_MESSAGE_LOG, message);
        LogHelper.i(TAG, message);
        //添加通信消息
        addMessage(packageName, newMessage);
    }

    public void addMessage(String packageName, Message message) {
        ClientMessageModel supportMessage = getSupportMessage(packageName);
        supportMessage.getMessageQueueWorkerThread().addMessage(message);
    }


}
