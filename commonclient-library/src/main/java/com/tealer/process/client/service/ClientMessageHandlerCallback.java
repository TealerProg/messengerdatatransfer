package com.tealer.process.client.service;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.tealer.process.client.ClientMessageManager;
import com.tealer.process.client.callback.Callback;
import com.tealer.process.client.config.ClientConstantConfig;
import com.tealer.process.client.request.RequestManager;


/**
 * 描述
 *
 * @author Created by lipengbo
 * @email 1162947801@qq.com
 * @time Created on 2022/11/10 19:54
 */
public class ClientMessageHandlerCallback implements Handler.Callback {

    private Context context;

    private ComponentName componentName;

    public ClientMessageHandlerCallback(Context context, ComponentName name) {
        this.context = context;
        this.componentName = name;
    }

    @Override
    public boolean handleMessage(@NonNull Message message) {
        switch (message.what) {
            case ClientConstantConfig.Common.RESPONSE_MESSAGE:
                //请求管理器响应消息
                responseMessage(context, message);
                break;
        }
        return true;
    }

    /**
     * 响应消息
     *
     * @param context
     * @param message
     */
    public void responseMessage(Context context, Message message) {
        //请求时间
        int requestId = message.arg1;
        int requestTime = message.arg2;
        Bundle data = message.getData();
        RequestManager.Call call = RequestManager.get().findCall(requestId, requestTime);
        if (null == call) {
            addLogMessage("ClientMessageManager[responseMessage] 查找不到请求!");
        } else {
            //设置intent的classloader,否则会引发 android.os.BadParcelableException: ClassNotFoundException
            data.setClassLoader(context.getClassLoader());
            Parcelable parcelable = data.getParcelable(ClientConstantConfig.Common.KEY_APP_MESSAGE_DATA);
            //请求回调
            Callback<Parcelable> callback = call.requestCallback;
            if (null != callback) {
                callback.call(context, parcelable);
            }
            //移除回调
            RequestManager.get().removeCall(call);
            //发送消息
            addLogMessage("ClientMessageManager[responseMessage] 回应请求成功!");
        }


    }

    /**
     * 添加一条日志消息
     * @param message
     */
    private void addLogMessage(String message) {
        //添加通信消息
        String packagName = componentName.getPackageName();
        ClientMessageManager.get().addLogMessage(packagName, message);
    }
}
