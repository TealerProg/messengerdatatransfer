package com.tealer.process.client.request;

import android.os.Message;
import android.os.Parcelable;

import com.tealer.process.client.ClientMessageManager;
import com.tealer.process.client.beat.HeartBeatTimer;
import com.tealer.process.client.callback.Callback;
import com.tealer.process.client.callback.ServiceConnectCallback;
import com.tealer.process.client.config.ClientConstantConfig;
import com.tealer.process.client.log.LogHelper;
import com.tealer.process.client.utils.ServiceHelperCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述
 *
 * @author Created by lipengbo
 * @email 1162947801@qq.com
 * @time Created on 2022/11/10 16:06
 */
public class RequestManager {

    public static final String TAG = RequestManager.class.getSimpleName();

    private final int COUNT_EXECUTE_TIMES = 5;

    /**
     * 请求处理对象
     */
    private List<Call> calls = new ArrayList<>();

    private final static RequestManager requestManager = new RequestManager();

    public static RequestManager get() {
        return requestManager;
    }

    public void request(String packageName, Message message, ServiceConnectCallback serviceConnectCallback) {
        //启动服务
        onStartService(packageName, serviceConnectCallback);
        //添加请求
        ClientMessageManager.get().addLogMessage(packageName,"RequestManager[request] 发送一个请求:"+message.arg1 + " 时间:" + message.arg2);
        //添加消息
        ClientMessageManager.get().addMessage(packageName,message);

    }

    private void onStartService(String packageName, ServiceConnectCallback serviceConnectCallback) {
        //判断服务是否连接
        if (!ClientMessageManager.get().isConnected(packageName)) {
            ClientMessageManager.get().startHeartBeatTimer(packageName, new HeartBeatTimer(300, COUNT_EXECUTE_TIMES) {
                @Override
                public void onTick(long currentExecuteTimes, long millisUntilFinished) {
                    boolean connected = ClientMessageManager.get().isConnected(packageName);
                    LogHelper.i(TAG, "Tick currentExecuteTimes:" + currentExecuteTimes + ", isConnected：" + connected);
                    if (!connected) {
                        ClientMessageManager.get().onServiceDisconnected(packageName);
                        if (currentExecuteTimes >= COUNT_EXECUTE_TIMES) {
                            if (null != serviceConnectCallback) {
                                serviceConnectCallback.onFailure();
                            }
                            ClientMessageManager.get().unbindServie(packageName);
                        } else {
                            LogHelper.w(TAG, "尝试重新启动服务，Service Action:" + ServiceHelperCompat.getServiceAction(packageName, ClientConstantConfig.Common.KEY_SERVICE_ACTION_SUFFIX));
                            ClientMessageManager.get().bindService(packageName);
                        }
                    }
                }
            });
        }
    }

    /**
     * 添加请求对象
     *
     * @param id
     * @param requestTime
     * @param callback
     */
    public void addCall(int id, int requestTime, Callback<Parcelable> callback) {
        synchronized (this) {
            this.calls.add(new Call(id, requestTime, callback));
        }
    }

    /**
     * 移除请求对象
     *
     * @param call
     */
    public void removeCall(Call call) {
        synchronized (this) {
            this.calls.remove(call);
        }
    }

    /**
     * 根据请求Id，请求时间获得对应的请求信息
     *
     * @param requestId
     * @param requestTime
     * @return
     */
    public Call findCall(int requestId, int requestTime) {
        for (Call call : calls) {
            if (call.requestId == requestId && call.requestTime == requestTime) {
                return call;
            }
        }
        return null;
    }


    /**
     * 一个请求对象
     */
    public class Call {

        public int requestId;
        public int requestTime;

        public Callback<Parcelable> requestCallback;

        public Call(int requestId, int requestTime, Callback<Parcelable> requestCallback) {
            this.requestId = requestId;
            this.requestTime = requestTime;
            this.requestCallback = requestCallback;
        }
    }
}
