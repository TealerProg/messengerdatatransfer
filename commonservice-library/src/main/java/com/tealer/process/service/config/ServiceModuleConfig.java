package com.tealer.process.service.config;

/**
 * 描述
 *
 * @author Created by lipengbo
 * @email 1162947801@qq.com
 * @time Created on 2022/11/6 15:56
 */
public class ServiceModuleConfig {

    public static class Common {
        /**
         * 传递订阅对象的Key(数据对象Key)
         */
        public static final String KEY_APP_MESSAGE_DATA = "key_app_message_data";
        /**
         * 传递订阅对象的Key(log对象Key)
         */
        public static final String StringKEY_APP_MESSAGE_LOG = "key_app_message_log";

        /**
         * 日志消息
         */
        public static final int LOG_MESSAGE = 1;

        /**
         * 转发消息，主要发送请求到服务内，由服务转发此消息
         */
        public static final int FORWARD_MESSAGE = 2;

        /**
         * 回应消息，用于处理完成请求
         */
        public static final int RESPONSE_MESSAGE = 3;
    }

    public class Constants {

        public static final String DISPATCH_REGIDTER_SERVICE_ACTION = "com.tealer.supercross.dispatch_register_service";

        public static final String DISPATCH_UNREGIDTER_SERVICE_ACTION = "com.tealer.supercross.dispatch_unregister_service";

        public static final String DISPATCH_EVENT_ACTION = "com.tealer.supercross.dispatch_event";
    }
}
