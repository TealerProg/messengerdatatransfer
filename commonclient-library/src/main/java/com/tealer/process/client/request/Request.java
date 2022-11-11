package com.tealer.process.client.request;

import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.os.SystemClock;

import com.tealer.process.client.callback.Callback;
import com.tealer.process.client.callback.ServiceConnectCallback;
import com.tealer.process.client.config.ClientConstantConfig;

/**
 * 一个跨进程请求
 *
 * @author Created by lipengbo
 * @email 1162947801@qq.com
 * @time Created on 2022/11/10 10:57
 */
public class Request {
    private int requestId;

    private ServiceConnectCallback serviceConnectCallback;

    public Request(int requestId) {
        this.requestId = requestId;
    }

    /**
     * 处理事件
     * 每个请求为一个Message
     *
     * @param packageName
     * @param params
     * @param callback
     * @param <T>
     */
    public <T extends Parcelable> void request(String packageName, Bundle params, Callback<T> callback) {
        //发送请求
        Message message = Message.obtain();
        //区别码
        message.what = ClientConstantConfig.Common.FORWARD_MESSAGE;
        //传入数据
        message.setData(params);
        //记录请求Id
        message.arg1 = requestId;
        //记录请求时间，用于区分多次发送请求
        long requestTime = SystemClock.elapsedRealtime();
        message.arg2 = (int) requestTime;
        //记录回调对象，用于关联此对象，发送后会清楚
        message.obj = callback;
       //添加到消息队列


    }


}
