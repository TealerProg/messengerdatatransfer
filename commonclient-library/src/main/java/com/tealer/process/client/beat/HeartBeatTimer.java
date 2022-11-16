package com.tealer.process.client.beat;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

import androidx.annotation.NonNull;

/**
 * 心跳探测，用于确定服务稳定性
 *
 * @author Created by lipengbo
 * @email 1162947801@qq.com
 * @time Created on 2022/11/9 16:46
 */
public abstract class HeartBeatTimer {

    /**
     * 计时起始时间
     */
    private final long startTimeMills;


    /**
     * 计时间隔
     */
    private final long countdownInterval;


    /**
     * 总执行次数
     */
    private long countExecuteTimes = 0;

    /**
     * 当前执行次数
     */
    private long currentExecuteTimes = 0;

    /**
     * 起始计时时间，用于获取时间间隔
     */
    private long currentTimeMills;

    /**
     * 标志是否取消当前任务
     */
    private boolean cancelled = false;


    private static final int MSG = 1;


    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            synchronized (HeartBeatTimer.class) {
                if (cancelled || (countExecuteTimes > 0 && (currentExecuteTimes + 1) > countExecuteTimes)) {
                    return;
                }
                //执行次数+1
                if(countExecuteTimes>0){
                    currentExecuteTimes++;
                }
                //获取经过时长
                long elapsedRealTime= SystemClock.elapsedRealtime();
                if(0==currentTimeMills){
                    currentTimeMills=elapsedRealTime;
                }
                final long millisLeft=startTimeMills+(elapsedRealTime-currentTimeMills);

                //回调时长
                onTick(currentExecuteTimes,millisLeft);
                //发送新的事件
                sendMessageDelayed(obtainMessage(MSG),countdownInterval);


            }
        }
    };



    public HeartBeatTimer(long countdownInterval) {
        this(0, countdownInterval, 0);
    }

    public HeartBeatTimer(long countdownInterval, long countExecuteTimes) {
        this(0, countdownInterval, countExecuteTimes);
    }

    /**
     * 构造函数
     *
     * @param startTimeMills    起始的计时时间,如给定 30000,30秒,则回调,会以30秒开始计时,用于数值从某一个时间点开始
     * @param countdownInterval The interval along the way to receive
     * @param countExecuteTimes
     */
    public HeartBeatTimer(long startTimeMills, long countdownInterval, long countExecuteTimes) {
        this.startTimeMills = startTimeMills;
        this.countdownInterval = countdownInterval;
        this.countExecuteTimes = countExecuteTimes;
    }

    public synchronized final HeartBeatTimer start(){
         this.cancelled=false;
         this.currentExecuteTimes=0;
         this.currentTimeMills=SystemClock.elapsedRealtime();
         this.handler.sendMessageDelayed(handler.obtainMessage(MSG),countdownInterval);
         return this;
    }

    /**
     *获取经过时长
     * @return
     */
    public final long getCountTimeMillis(){
       return startTimeMills+(SystemClock.elapsedRealtime()-currentTimeMills);
    }

    /**
     * 取消当前计数
     */
    public synchronized final  void cancel(){
       this.cancelled=true;
       this.handler.removeMessages(MSG);
    }



    /**
     * Callback fired on regular interval.
     * @param currentExecuteTimes
     * @param millisUntilFinished  The amount of time until finished.
     */
    public abstract void onTick(long currentExecuteTimes, long millisUntilFinished);


}
