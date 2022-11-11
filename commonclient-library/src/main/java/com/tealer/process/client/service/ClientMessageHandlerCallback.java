package com.tealer.process.client.service;

import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;


/**
 * 描述
 *
 * @author Created by lipengbo
 * @email 1162947801@qq.com
 * @time Created on 2022/11/10 19:54
 */
public class ClientMessageHandlerCallback implements Handler.Callback {

    public ClientMessageHandlerCallback(Context context, ComponentName name) {
    }

    @Override
    public boolean handleMessage(@NonNull Message message) {
        return false;
    }
}
