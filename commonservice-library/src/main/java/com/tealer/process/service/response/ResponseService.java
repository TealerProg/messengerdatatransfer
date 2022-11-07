package com.tealer.process.service.response;

import android.content.Context;
import android.os.Bundle;

/**
 * 服务Model对象，用于处理通信事件
 *
 * @author Created by lipengbo
 * @email 1162947801@qq.com
 * @time Created on 2022/11/6 18:30
 */
public interface ResponseService {
    /**
     *声明请求Id
     * @return
     */
    public int getRequestId();

    /**
     *是否应用此Id
     * @param id
     * @return
     */
    public boolean isApply(int id);


    /**
     *回应消息并处理
     * @param context
     * @param params
     */
    public abstract void run(Context context, Bundle params);
}
