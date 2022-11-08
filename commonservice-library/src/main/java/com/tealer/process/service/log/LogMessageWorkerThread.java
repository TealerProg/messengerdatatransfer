package com.tealer.process.service.log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;

/**
 * 文件写入 服务对象
 *
 * @author Created by lipengbo
 * @email 1162947801@qq.com
 * @time Created on 2022/11/8 15:26
 */
public class LogMessageWorkerThread extends Thread {
    /**
     * 最大只存100条，100条之后阻塞
     */
    private final static int MAX_CAPACITY = 1;

    /**
     * 最长记录等待时间，保证记录时间偏差不大
     */
    private final static long MAX_WAIT_TIME = 2 * 60 * 1000L;

    /**
     * 同步锁对象
     */
    private Object LOCK = new Object();
    /**
     * 日期格式化对象
     */
    private SimpleDateFormat dataFormatter = new SimpleDateFormat("HH:mm:ss");
    /**
     * 消息队列
     */
    private Deque<String> messageQueue = new LinkedList<>();

    /**
     * 操作时间，用于比较最大时间
     */
    private long activeTimeMillis = 0L;

    /**
     * 消息输出监听
     */
    private List<OnMessageListener> listenerList = new ArrayList<>();

    /**
     * 当前服务是否运行
     */
    private volatile boolean isRunning = false;


    public LogMessageWorkerThread() {
        this("MessageManager worker");
    }

    public LogMessageWorkerThread(String name) {
        super(name);
    }


    @Override
    public void run() {
        super.run();
        try {
            //遍历消息
            while (!interrupted()) {
                flushMessageAndWait();
                if (!isRunning) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //写入剩下所有文件
            flushMessage();
        }
    }

    private void flushMessage() {
        if (messageQueue != null && messageQueue.size() > 0) {
            //取出所有消息并记录
            while (!messageQueue.isEmpty()) {
                String message = messageQueue.pollFirst();
                for (OnMessageListener listener : listenerList) {
                    listener.onMessage(message);
                }
            }
        }
    }

    private void flushMessageAndWait() {
        if (messageQueue != null && messageQueue.size() > 0) {
            synchronized (LOCK) {
                while (!messageQueue.isEmpty()) {
                    String message = messageQueue.pollFirst();
                    for (OnMessageListener listener : listenerList) {
                        listener.onMessage(dataFormatter.format(new Date()) + "  " + message);
                    }
                }
                //记录操作时间
                activeTimeMillis = System.currentTimeMillis();
                try {
                    //等待
                    LOCK.wait(MAX_WAIT_TIME);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * 启动服务
     */
    public void startService() {
        //启动服务
        if (!isAlive()) {
            //记录启动时间
            activeTimeMillis = System.currentTimeMillis();
            //设置运行标记
            isRunning = true;
            this.start();
        }
    }


    /**
     * 停止服务
     */
    public void stopService() {
        synchronized (LOCK) {
            isRunning = false;
            LOCK.notify();
        }
    }


    public void postMessage(String message) {
        synchronized (LOCK) {
            //添加消息到队列
            messageQueue.offerLast(message);
            if (System.currentTimeMillis() - activeTimeMillis > MAX_WAIT_TIME) {
                //超过最大时间通知
                LOCK.notify();
            } else if (messageQueue.size() >= MAX_CAPACITY) {
               //超过最大个数通知
               LOCK.notify();
            }
        }
    }

    /**
     * 主动通知唤醒
     */
    public void notifyService(){
       synchronized (LOCK){
          LOCK.notify();
       }
    }

    /**
     * 设置消息输出监听
     * @param listener
     */
    public void addOnMessageListener(OnMessageListener listener){
       synchronized (LOCK){
           this.listenerList.add(listener);
       }
    }

    /**
     * 移除消息通知监听
     * @param listener
     */
    public void removeOnMessageListner(OnMessageListener listener){
        synchronized (LOCK){
           this.listenerList.remove(listener);
        }
    }

    /**
     * 输出消息监听
     */
    interface OnMessageListener {
        void onMessage(String message);
    }


}
