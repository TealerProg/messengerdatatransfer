package com.tealer.process.client.callback;

import android.os.Messenger;

/**
 * 服务响应的事件回调
 *
 * @author Created by lipengbo
 * @email 1162947801@qq.com
 * @time Created on 2022/11/9 16:36
 */
public interface ServiceCallback {

    /**
     * 服务连接成功
     * @param serviceMessenger
     * @param replyMessenger
     */
   void onServcieConnected(Messenger  serviceMessenger,Messenger replyMessenger);


    /**
     *服务连接中断
     */
   void onServiceConnected();


}
