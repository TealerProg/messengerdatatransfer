package com.tealer.messengerdatatransfer

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tealer.config.network.data.model.BaseResponseEntity
import com.tealer.messengerdatatransfer.databinding.ActivityClientMessengerBinding
import com.tealer.messengerdatatransfer.databinding.ActivityClientMessengerSimpleLayoutBinding
import com.tealer.process.client.callback.Callback
import com.tealer.process.client.request.Request
import kotlin.concurrent.thread

/**
 *描述
 *@author Created by lipengbo
 *@email 1162947801@qq.com
 *@time Created on 2022/11/14 11:52
 */
class ClientMessengerSimpleActivity:AppCompatActivity() {
    var count = 0;
    private lateinit var binding: ActivityClientMessengerSimpleLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityClientMessengerSimpleLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //创建请求体,注意此处:100是请求id,响应处理请求100的请求
        val userInfoRequest = Request(100)
        //设置服务器端连接监听
        userInfoRequest.setServiceConnectCallback {
            binding.userInfoView.text = "连接超时！"
            Toast.makeText(baseContext,"连接超时了！", Toast.LENGTH_SHORT).show()

        }

        binding.getUserInfoView.setOnClickListener{
            thread {
                // 发送请求参数,没有可以省略
                val params = Bundle()
                params.putInt("count", count++)
                // 发送请求,请求附回调结果
                userInfoRequest.request("com.tealer.simple_service", params,
                    Callback<BaseResponseEntity> { context, data ->
                        println("data:$data")
                        //设置用户信息
                        binding.userInfoView.text = data.data
                    })
            }
        }
    }
}