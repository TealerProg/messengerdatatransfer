package com.tealer.process.client.callback;

import android.content.Context;
import android.os.Parcelable;

/**
 * 发起request请求后，response响应的回调接口
 *
 * @author Created by lipengbo
 * @email 1162947801@qq.com
 * @time Created on 2022/11/9 15:50
 */
public interface Callback<T extends Parcelable> {

    /**
     * 回调请求对象
     * @param context
     * @param data
     */
   void call(Context context,T data);


}
