package com.tealer.process.service;

import com.tealer.process.service.response.ResponseService;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述
 *
 * @author Created by lipengbo
 * @email 1162947801@qq.com
 * @time Created on 2022/11/6 21:32
 */
public class ResponseServiceListManager {

    private final static List<ResponseService> responseServiceLists = new ArrayList<>();

    /**
     * 通过插件加载实现了ResponseService接口的类
     *
     * @param responseService
     */
    public static void register(ResponseService responseService) {
        System.out.println("走了方法");
        if (responseService != null) {
            responseServiceLists.add(responseService);

        }
    }

    public static List<ResponseService> getResponseServiceLists() {
        return responseServiceLists;
    }


}
