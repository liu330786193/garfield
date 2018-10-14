package com.lyl.garfield.plugin.elasticsearch.v5;

import com.lyl.garfield.api.component.ComponentsDefine;
import com.lyl.garfield.api.trace.AbstractSpan;
import com.lyl.garfield.api.trace.tag.Tags;
import com.lyl.garfield.core.context.ContextManager;
import com.lyl.garfield.core.plugin.interceptor.enhance.EnhancedInstance;
import com.lyl.garfield.core.plugin.interceptor.enhance.InstanceMethodsAroundInterceptor;
import com.lyl.garfield.core.plugin.interceptor.enhance.MethodInterceptResult;

import java.lang.reflect.Method;

public class ElasticsearchRestInterceptor implements InstanceMethodsAroundInterceptor {

    @Override
    public void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, MethodInterceptResult result) throws Throwable {
        AbstractSpan span = ContextManager.createLocalSpan("Elasticsearch/RestClient");
        span.setComponent(ComponentsDefine.ELASTICSEARCH);
        Tags.URL.set(span, (String) allArguments[1]);
        Tags.HTTP.METHOD.set(span, (String) allArguments[0]);
    }

    @Override
    public Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, Object ret) throws Throwable {
        ContextManager.stopSpan();
        return ret;
    }

    @Override
    public void handleMethodException(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, Throwable t) {
        ContextManager.activeSpan().errorOccurred().log(t);
    }

}
