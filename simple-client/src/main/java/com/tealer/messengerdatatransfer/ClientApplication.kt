package com.tealer.messengerdatatransfer

import android.app.Application
import android.util.Log
import com.tealer.process.client.ClientMessageManager
import com.tealer.process.client.log.SimpleLogDelegate

/**
 *描述
 *@author Created by lipengbo
 *@email 1162947801@qq.com
 *@time Created on 2022/11/14 11:14
 */
class ClientApplication : Application() {


    override fun onCreate() {
        super.onCreate()

        ClientMessageManager.get()
            .init(this)
            //Log开关。建议测试环境开启，线上环境应该关闭。
            .setEnableLog(BuildConfig.DEBUG)
            //Log扩展接口，方便做日志输出定制（不设置，默认使用DefaultLogDelegate）
            .setLogDelegate(object : SimpleLogDelegate() {
                override fun i(tag: String?, msg: String) {
                    Log.i(tag, msg)
                }

                override fun w(tag: String?, msg: String) {
                    Log.w(tag, msg)
                }
            })
    }
}