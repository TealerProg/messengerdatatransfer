package com.tealer.process.client;

import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.tealer.process.client.callback.Callback;
import com.tealer.process.client.config.ClientConstantConfig;
import com.tealer.process.client.log.LogHelper;
import com.tealer.process.client.request.RequestManager;

import java.util.LinkedList;

/**
 * 描述
 *
 * @author Created by lipengbo
 * @email 1162947801@qq.com
 * @time Created on 2022/11/11 14:08
 */
public class MessageQueueWorkerThread extends Thread {

    /**
     * 同步锁对象
     */
    private final Object LOCK = new Object();

    /**
     * 当前消息队列
     */
    private LinkedList<Message> messageQueue = new LinkedList<>();

    /**
     * 通信对象，如果为空会阻塞当前线程，默认阻塞
     */
    private Messenger messenger = null;
    private Messenger replyMessenger = null;

    public MessageQueueWorkerThread() {
        this("message thread");
    }

    public MessageQueueWorkerThread(String name) {
        super(name);
    }


    public void setMessenger(Messenger serviceMessenger, Messenger replyMessenger) {
        synchronized (LOCK) {
            this.messenger = serviceMessenger;
            this.replyMessenger = replyMessenger;
            if (null != messenger && null != replyMessenger) {
                LOCK.notifyAll();
            }
        }
    }

    /**
     * 添加请求消息
     *
     * @param message
     */
    public void addMessage(Message message) {
        synchronized (LOCK) {
            messageQueue.offer(message);
            if (null != messenger && null != replyMessenger) {
                LOCK.notifyAll();
            }
        }

    }

    @Override
    public void run() {
        super.run();
        try {
            //遍历消息
            while (!interrupted()) {
                synchronized (LOCK) {
                    //尝试获取消息
                    while (!messageQueue.isEmpty()) {
                        //检测是否需要阻塞当前线程
                        if (null == messenger || null == replyMessenger) {
                            LOCK.wait();
                        }
                        //检测是否服务通道建立成功
                        if (!isBinderAlive()) {
                            LOCK.wait();
                        }
                        Message message = messageQueue.pollFirst();
                        //发送消息
                        sendMessage(message);
                    }
                    LOCK.wait();
                }
            }
        } catch (Exception e) {

        } finally {

        }
    }

    /**
     * 发送日志消息
     *
     * @param message
     */
    public void sendMessage(String message) {
        if (null == message) {
            return;
        }
        //服务终止发送消息
        Message newMessage = Message.obtain();
        newMessage.what = ClientConstantConfig.Common.LOG_MESSAGE;
        newMessage.getData().putString(ClientConstantConfig.Common.KEY_APP_MESSAGE_LOG, message);
        //添加通信消息
        sendMessage(newMessage);
    }

    private Boolean isBinderAlive() {
        IBinder binder = (null != messenger) ? messenger.getBinder() : null;
        return binder != null ? binder.isBinderAlive() : false;
    }


    /**
     * 停止线程
     */
    public void onStop() {
        messageQueue.clear();
        this.interrupt();
    }


    public void sendMessage(Message message) {
        message.replyTo = replyMessenger;
        switch (message.what) {
            case ClientConstantConfig.Common.FORWARD_MESSAGE:
                //获取回调对象
                Callback callback = (Callback) message.obj;
                message.obj = null;
                try {
                    if (null != messenger) {
                        messenger.send(message);
                    }
                } catch (RemoteException e) {
                    sendMessage(e.getMessage());
                }
                //添加一个请求
                RequestManager.get().addCall(message.arg1, message.arg2,callback);
                break;
            default:
                try {
                    if (null != messenger) {
                        messenger.send(message);
                    }
                } catch (RemoteException e) {
                    sendMessage(e.getMessage());
                }
                break;
        }


    }


}
