package com.tealer.process.client.config;

/**
 * 全局常量配置信息
 *
 * @author Created by lipengbo
 * @email 1162947801@qq.com
 * @time Created on 2022/11/9 15:08
 */
public class ClientConstantConfig {

    public static class Common{
        /**
         * 服务端 Messager Service的Action后缀名称
         */
        public static final String KEY_SERVICE_ACTION_SUFFIX=".authenticator.service";

        /**
         *传递订阅对象的Key
         */
        public static final String KEY_APP_MESSAGE_DATA="key_app_message_data";
        /**
         *传递订阅对象的Key
         */
        public static final String KEY_APP_MESSAGE_LOG="key_app_message_log";
        /**
         *日志消息
         */
        public static final int LOG_MESSAGE=1;

        /**
         *转发消息，主要发送请求到服务内，由服务转发此消息
         */
        public static final int FORWARD_MESSAGE=2;

        /**
         *回应消息，用于处理完请求后
         */
        public static final int RESPONSE_MESSAGE=3;
    }


}
