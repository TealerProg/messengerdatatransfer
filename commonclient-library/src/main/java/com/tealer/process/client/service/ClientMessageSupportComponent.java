package com.tealer.process.client.service;

import android.content.Context;
import android.os.Message;
import android.os.Messenger;
import android.util.ArrayMap;

import com.tealer.process.client.MessageQueueWorkerThread;
import com.tealer.process.client.callback.ServiceCallback;
import com.tealer.process.client.config.ClientConstantConfig;
import com.tealer.process.client.log.LogHelper;

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

    /**
     * 添加一条日志消息
     *
     * @param packageName
     * @param message
     */
    private void addLogMessage(String packageName, String message) {
        //添加消息
        Message newMessage = Message.obtain();
        newMessage.what = ClientConstantConfig.Common.LOG_MESSAGE;
        newMessage.getData().putString(ClientConstantConfig.Common.KEY_APP_MESSAGE_LOG,message);
        LogHelper.i(TAG,message);
        //添加通信消息
        addMessage(packageName,newMessage);
    }

    public void addMessage(String packageName,Message message){
         ClientMessageModel supportMessage=getSupportMessage(packageName);
         supportMessage.getMessageQueueWorkerThread().addMessage(message);
    }


}
