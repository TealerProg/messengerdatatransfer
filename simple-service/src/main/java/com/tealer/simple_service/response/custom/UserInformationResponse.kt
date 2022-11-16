package com.tealer.simple_service.response.custom

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.widget.Toast
import com.tealer.config.network.data.model.BaseResponseEntity
import com.tealer.process.service.response.ResponseResultService
import com.tealer.simple_service.json.GsonUtils
import java.util.HashMap
import kotlin.concurrent.thread

/**
 *描述
 *@author Created by lipengbo
 *@email 1162947801@qq.com
 *@time Created on 2022/11/14 20:07
 */
class UserInformationResponse :ResponseResultService(){
    override fun getRequestId(): Int {
        return 100
    }

    override fun run(context: Context?, response: Response?, params: Bundle?) {
        thread {
            SystemClock.sleep(1000)
            val count=params?.getInt("count")?:0

            // 模拟数据
            val data: MutableMap<String, Any> = HashMap()
            data["name"] = "小明"
            data["age"] = 18 + count
            data["count"] = count

            //为rom提供数据
            val responseEntity = BaseResponseEntity()
            responseEntity.httpCode = 200
            responseEntity.data = GsonUtils.toJson(data)
            responseEntity.meta = BaseResponseEntity.Meta(0, null)

            Handler(Looper.getMainLooper()).post {
                Toast.makeText(context,"来自服务端反馈的数据：${responseEntity}", Toast.LENGTH_LONG).show()
            }

            //响应消息
            response?.response(responseEntity)
        }
    }
}