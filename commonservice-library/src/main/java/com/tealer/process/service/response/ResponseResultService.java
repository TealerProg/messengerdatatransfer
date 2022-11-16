package com.tealer.process.service.response;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.os.RemoteException;

import androidx.annotation.NonNull;

import com.tealer.process.service.config.ServiceModuleConfig;

/**
 * 服务Model对象
 *
 * @author Created by lipengbo
 * @email 1162947801@qq.com
 * @time Created on 2022/11/7 14:18
 */
public abstract class ResponseResultService implements ResponseService {

    /**
     * 回复消息体
     */
    private Message replyMessage = null;

    @Override
    public boolean isApply(int id) {
        return this.getRequestId() == id;
    }

    /**
     * 设置本次请求回复的消息体
     *
     * @param message
     */
    public void setReplyMessage(Message message) {
        this.replyMessage = message;
    }


    @Override
    public void run(Context context, Bundle params) {
       //创建一个新的响应请求体
      Response newResponse=newResponse();
      //设置响应消息体
      newResponse.setReplyMessage(replyMessage);
      //执行事件
      run(context,newResponse,params);

    }

    private Response newResponse() {
        return new Response();
    }

    /**
     * 执行异步请求
     */
    public abstract void run(Context context,Response response,Bundle params);


    public class Response implements Cloneable {
        /**
         * 回复消息体
         */
        private Message replyMessage = null;

        public void setReplyMessage(Message message) {
            this.replyMessage = Message.obtain(message);
        }

        public <T extends Parcelable> void response(T item) {
            Message newMessage = Message.obtain(replyMessage);
            //设置消息分类为回应
            newMessage.what = ServiceModuleConfig.Common.RESPONSE_MESSAGE;
            //放入数据
            Bundle data = new Bundle();
            data.putParcelable(ServiceModuleConfig.Common.KEY_APP_MESSAGE_DATA, item);
            newMessage.setData(data);
            try {
                Messenger client = replyMessage.replyTo;
                client.send(newMessage);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @NonNull
        @Override
        protected Response clone() throws CloneNotSupportedException {
            return (Response) super.clone();
        }
    }
}
