package com.lyl.garfield.plugin.activemq.v5;

import com.lyl.garfield.api.component.ComponentsDefine;
import com.lyl.garfield.api.trace.AbstractSpan;
import com.lyl.garfield.api.trace.SpanLayer;
import com.lyl.garfield.api.trace.tag.Tags;
import com.lyl.garfield.core.context.ContextManager;
import com.lyl.garfield.core.plugin.interceptor.enhance.EnhancedInstance;
import com.lyl.garfield.core.plugin.interceptor.enhance.InstanceMethodsAroundInterceptor;
import com.lyl.garfield.core.plugin.interceptor.enhance.MethodInterceptResult;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.lang.reflect.Method;

public class ActiveMQConnectInterceptor implements InstanceMethodsAroundInterceptor {

    public static final String ACTIVE_MQ_OPERATION_NAME_PREFIX = "ActiveMQ/";

    @Override
    public void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, MethodInterceptResult result) throws Throwable {
        ActiveMQConnectionFactory factory = (ActiveMQConnectionFactory) objInst;
        AbstractSpan activeSpan = ContextManager.createLocalSpan(buildOperationName(factory));
        activeSpan.setComponent(ComponentsDefine.ACTIVE_MQ);
        SpanLayer.asMQ(activeSpan);
    }

    @Override
    public Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, Object ret) throws Throwable {
        ContextManager.stopSpan();
        return ret;
    }

    @Override
    public void handleMethodException(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, Throwable t) {
        AbstractSpan span = ContextManager.activeSpan();
        span.log(t).errorOccurred();
        Tags.STATUS_CODE.set(span, "500");
    }

    private String buildOperationName(ActiveMQConnectionFactory factory){
        return new StringBuffer().append(ACTIVE_MQ_OPERATION_NAME_PREFIX)
                .append(ActiveMQConnectionFactory.DEFAULT_BROKER_URL)
                .append("/Connect")
                .toString();
    }
}
