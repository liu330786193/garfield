package com.lyl.garfield.plugin.elastic.job.v2;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.google.common.base.Strings;
import com.lyl.garfield.api.component.ComponentsDefine;
import com.lyl.garfield.api.trace.AbstractSpan;
import com.lyl.garfield.core.context.ContextManager;
import com.lyl.garfield.core.plugin.interceptor.enhance.EnhancedInstance;
import com.lyl.garfield.core.plugin.interceptor.enhance.InstanceMethodsAroundInterceptor;
import com.lyl.garfield.core.plugin.interceptor.enhance.MethodInterceptResult;

import java.lang.reflect.Method;

public class ElasticJobSimpleJobInterceptor implements InstanceMethodsAroundInterceptor {


    @Override
    public void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes,
                             MethodInterceptResult result) throws Throwable {
        ShardingContexts shardingContexts = (ShardingContexts)allArguments[0];
        Integer item = (Integer)allArguments[1];
        ShardingContext shardingContext = new ShardingContext(shardingContexts, item);
        String operateName = shardingContext.getJobName();
        if (!Strings.isNullOrEmpty(shardingContext.getShardingParameter())) {
            operateName += "-" + shardingContext.getShardingParameter();
        }
        AbstractSpan span = ContextManager.createLocalSpan(operateName);
        span.setComponent(ComponentsDefine.ELASTIC_JOB);
        span.tag("sharding_context", shardingContext.toString());
    }

    @Override
    public Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes,
                              Object ret) throws Throwable {
        ContextManager.stopSpan();
        return ret;
    }

    @Override public void handleMethodException(EnhancedInstance objInst, Method method, Object[] allArguments,
                                                Class<?>[] argumentsTypes, Throwable t) {
        ContextManager.activeSpan().errorOccurred().log(t);
    }

}
