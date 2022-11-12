package com.tealer.process.client.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.List;

/**
 * 描述
 *
 * @author Created by lipengbo
 * @email 1162947801@qq.com
 * @time Created on 2022/11/12 15:36
 */
public class ServiceHelperCompat {

    /**
     * Intent隐式转显式
     * 适配安卓5.0以上
     *
     * @param context        上下文
     * @param implicitIntent 隐式Intent
     * @return
     */
    public static Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);
        Intent explicitIntent = new Intent(implicitIntent);
        explicitIntent.setComponent(component);
        return explicitIntent;
    }

    /**
     * 获取 Service 的 action
     * @param packageName
     * @param actionSuffix ClientConstantConfig.Common.KEY_SERVICE_ACTION_SUFFIX
     * @return
     */
    public static String getServiceAction(String packageName,String actionSuffix){
        StringBuilder actionStringBuilder = new StringBuilder();
        if(packageName!= null){
            actionStringBuilder.append(packageName);
            actionStringBuilder.append(actionSuffix);
        }
        return actionStringBuilder.toString();
    }

}
