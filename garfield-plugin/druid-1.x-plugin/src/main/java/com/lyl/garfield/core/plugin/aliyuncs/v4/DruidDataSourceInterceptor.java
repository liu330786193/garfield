package com.lyl.garfield.core.plugin.aliyuncs.v4;

import core.plugin.interceptor.enhance.EnhancedInstance;
import core.plugin.interceptor.enhance.InstanceMethodsAroundInterceptor;
import core.plugin.interceptor.enhance.MethodInterceptResult;

import java.lang.reflect.Method;

public class DruidDataSourceInterceptor implements InstanceMethodsAroundInterceptor {
    @Override
    public void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, MethodInterceptResult result) throws Throwable {
        System.out.println("Druid");
    }

    @Override
    public Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, Object ret) throws Throwable {
        return ret;
    }

    @Override
    public void handleMethodException(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, Throwable t) {

    }
}
