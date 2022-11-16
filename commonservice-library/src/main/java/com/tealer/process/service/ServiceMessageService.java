package com.tealer.process.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tealer.process.service.config.ServiceModuleConfig;
import com.tealer.process.service.log.LogMessageManager;
import com.tealer.process.service.response.ResponseResultService;
import com.tealer.process.service.response.ResponseService;

import java.util.ArrayList;
import java.util.List;

/**
 * 当前组件通信Service对象
 *
 * @author Created by lipengbo
 * @email 1162947801@qq.com
 * @time Created on 2022/11/6 18:15
 */
public class ServiceMessageService extends Service {

    /**
     * 全局上下文对象
     */
    private Context applicationContext;

    /**
     * 注册model对象
     */
    private final List<ResponseService> registerInterfaceList = new ArrayList<>();


    private Handler serviceHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what) {
                case ServiceModuleConfig.Common.LOG_MESSAGE:
                    //日志消息
                    String msg = message.getData().getString(ServiceModuleConfig.Common.StringKEY_APP_MESSAGE_LOG);
                    if (!TextUtils.isEmpty(msg)) {
                        LogMessageManager.post(msg);
                    }
                    break;
                case ServiceModuleConfig.Common.FORWARD_MESSAGE:
                    //转发消息，当前作为消息中转
                    int requestId = message.arg1;
                    for (ResponseService model : registerInterfaceList) {
                        if (model.isApply(requestId)) {
                            //如果返回结果
                            if (model instanceof ResponseResultService) {
                                //设置回复消息
                                ((ResponseResultService) model).setReplyMessage(message);
                                //执行事件
                                model.run(applicationContext, message.getData());
                                //清空 replyMessage
                                ((ResponseResultService) model).setReplyMessage(null);
                                  //回复结果
                                LogMessageManager.post("ServicesMessageService:[handleMessage] 执行异步事件:"+message.arg1+" 请求时间:"+message.arg2);
                            }else{
                                //执行事件
                                model.run(applicationContext,message.getData());
                                //无需操作
                                LogMessageManager.post("ServicesMessageService:[handleMessage] 执行事件:"+message.arg1+" 请求时间:"+message.arg2);
                            }

                        }
                    }
                    break;
                default:
                    break;
            }


            return true;
        }
    });

    private Messenger serviceMessenger = new Messenger(serviceHandler);


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return serviceMessenger.getBinder();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        //记录上下文对象
        applicationContext = getApplication();
        //初始化数据
        init();

    }

    /**
     * 初始化注册服务对象
     */
    private void init() {
        if (registerInterfaceList != null && registerInterfaceList.size() > 0) {
            registerInterfaceList.clear();
        }
        //获得注册数据列
        List<ResponseService> responseServiceLists = ResponseServiceListManager.getResponseServiceLists();

        if (responseServiceLists != null && responseServiceLists.size() > 0) {
            for (ResponseService response : responseServiceLists) {
                //注册请求接口
                registerInterface(response);
            }
        } else {
            //TODO  Log日志输出
            LogMessageManager.post("初始化注册服务对象失败，responseServiceLists is null,未配置响应服务类!");
        }
    }

    /**
     * 注册一个ServiceModel对象
     *
     * @param model
     */
    private void registerInterface(ResponseService model) {
        ResponseService serviceModel = findResponseService(model.getRequestId());
        if (null != serviceModel) {

        } else {
            this.registerInterfaceList.add(model);
        }
    }

    private ResponseService findResponseService(int requestId) {
        for (ResponseService model : this.registerInterfaceList) {
            if (model.getRequestId() == requestId) {
                return model;
            }
        }
        return null;
    }

    /**
     * 取消注册一个ServiceModel对象
     *
     * @param model
     */
    private void unregisterInterface(ResponseService model) {
        this.registerInterfaceList.remove(model);
    }


}
